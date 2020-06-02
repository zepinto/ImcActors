package pt.lsts.imcactors.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;

public class IniConfiguration {

    private LinkedHashMap<String, IniSection> config = new LinkedHashMap<>();

    public IniConfiguration(File iniFile) throws IOException {
        apply(iniFile, this);
    }

    public IniConfiguration() {

    }

    public Set<String> sections() {
        return Collections.unmodifiableSet(config.keySet());
    }

    public Map<String, String> getParams(String section) {
        IniSection s = config.get(section);
        if (s == null)
            return new LinkedHashMap<>();

        return Collections.unmodifiableMap(s.config);
    }

    public String get(String section, String parameter) {
        if (config.containsKey(section))
            return config.get(section).get(parameter);
        return null;
    }

    public Map<String, String> get(String section) {
        if (!config.containsKey(section))
            return new HashMap<>();
        return config.get(section).getConfig();
    }

    public void set(String section, String parameter, String value) {
        set(section, Collections.singletonMap(parameter, value));
    }

    public void set(String section, Map<String, Object> values) {
        IniSection newSection = new IniSection(section);
        values.forEach((k,v) -> newSection.set(k,v));
        if (config.containsKey(section))
            config.get(section).apply(newSection);
        else
            config.put(section, newSection);
    }

    private void set(IniSection section) {
        if (config.containsKey(section.getName()))
            config.get(section.getName()).apply(section);
        else
            config.put(section.getName(), section);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        TreeSet<String> sections = new TreeSet<>();
        sections.addAll(config.keySet());
        sections.forEach(s -> {
            builder.append(config.get(s));
            builder.append("\n");
        });

        return builder.toString();
    }

    private static void apply(final File ini, final IniConfiguration wrapper) throws IOException  {
        ArrayList<String> currentSection = new ArrayList<>();

        Files.lines(ini.toPath()).map(l -> l.trim()).forEach(l -> {
            // skip comments and empty lines
            if (!l.isEmpty() && ! l.startsWith("#")) {
                // intercept section headers
                if (l.trim().startsWith("[")) {
                    String sectionHeader = parseSectionHeader(l);
                    if (sectionHeader.startsWith("Require")) {
                        File required = new File(ini.getParent(), sectionHeader.split(" ")[1]);
                        try { apply(required, wrapper); }
                        catch (Exception e) { e.printStackTrace(); }
                    }
                    else {
                        if (!currentSection.isEmpty()) {
                            wrapper.set(new IniSection(ini, currentSection));
                        }
                        currentSection.clear();
                        currentSection.add(l);
                    }
                }
                else {
                    currentSection.add(l);
                }
            }
        });
        if (!currentSection.isEmpty())
            wrapper.set(new IniSection(ini, currentSection));
    }

    private static String parseSectionHeader(String line) {
        try {
            return line.substring(line.indexOf("[")+1, line.indexOf("]")).trim();
        }
        catch (Exception e) {
            System.err.println(line+" is not a valid header.");
            return "invalid";
        }
    }

    public void save(File f) throws IOException {
        Files.write(f.toPath(), toString().getBytes());
    }

    public void removeUnused(final String profile) {
        removeSections(section -> {
            String val = section.get("Enabled");
            if (val == null)
                return false;
            if ("Never".equals(val) || (!"Always".equals(val) && !profile.equals(val)))
                return true;
            return false;
        });
    }

    public void removeSections(Predicate<IniSection> removePredicate) {
        config.values().removeIf(removePredicate);
    }

    static class IniSection {
        private String sectionName;
        private LinkedHashMap<String, String> config = new LinkedHashMap<>();

        public IniSection(File f, ArrayList<String> lines) {
            String firstLine = lines.get(0);
            sectionName = parseSectionHeader(firstLine).trim();
            String key = "", value = "";

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.contains("#"))
                    line = line.substring(0, line.indexOf("#")-1);
                if (line.contains("=")) {
                    if (!key.isEmpty())
                        set(key, value);

                    key = line.substring(0, line.indexOf("=")-1).trim();
                    value = line.substring(line.indexOf("=")+1).trim();
                }
                else {
                    value += " "+line.trim();
                }
            }
            if (!key.isEmpty())
                set(key, value);
        }

        public IniSection(String name) {
            this.sectionName = name;
        }

        public String getName() {
            return sectionName;
        }

        public String hasConfig(String key) {
            return config.get(key);
        }

        public String get(String key) {
            return config.get(key);
        }

        public String set(String key, Object value) {
            if (value == null)
                return delete(key);
            return config.put(key, value.toString());
        }

        public String delete(String key) {
            return config.remove(key);
        }

        public void apply(IniSection otherSection) {
            otherSection.config.forEach((k,v) -> set(k,v));
        }

        public Map<String, String> getConfig() {
            return Collections.unmodifiableMap(config);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("["+sectionName+"]\n");
            config.forEach((k,v) -> builder.append("   "+k+" = "+v+"\n"));
            return builder.toString();
        }
    }
}