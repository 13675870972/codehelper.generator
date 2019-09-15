/*
 *  Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.api;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * This class allows the code generator to be run from the command line.
 *
 * @author Jeff Butler
 */
public class ShellRunner {
    private static final String CONFIG_FILE = "-configfile";
    private static final String OVERWRITE = "-overwrite";
    private static final String CONTEXT_IDS = "-contextids";
    private static final String TABLES = "-tables";
    private static final String VERBOSE = "-verbose";
    private static final String FORCE_JAVA_LOGGING = "-forceJavaLogging";
    private static final String HELP_1 = "-?";
    private static final String HELP_2 = "-h";

    public static void run(AnActionEvent event, Configuration config, Set<String> fullyqualifiedTables) {

//        //此处写死参数
//        String[] args = {CONFIG_FILE, "generatorConfig.xml", OVERWRITE};
//
//        if (args.length == 0) {
//            usage();
//            System.exit(0);
//            return; // only to satisfy compiler, never returns
//        }
//
//        Map<String, String> arguments = parseCommandLine(args);
//
//        if (arguments.containsKey(HELP_1)) {
//            usage();
//            System.exit(0);
//            return; // only to satisfy compiler, never returns
//        }
//
//        if (!arguments.containsKey(CONFIG_FILE)) {
//            writeLine(getString("RuntimeError.0"));
//            return;
//        }
//
        List<String> warnings = new ArrayList<String>();
//
////        String configfile = arguments.get(CONFIG_FILE);
//        String configfile = "/Users/cyc/java/workspace/codehelper.generator/generatorConfig.xml";
//        File configurationFile = new File(configfile);
//        if (!configurationFile.exists()) {
//            writeLine(getString("RuntimeError.1", configfile));
//            return;
//        }
//
//        Set<String> fullyqualifiedTables = new HashSet<String>();
//        if (arguments.containsKey(TABLES)) {
//            StringTokenizer st = new StringTokenizer(arguments.get(TABLES), ",");
//            while (st.hasMoreTokens()) {
//                String s = st.nextToken().trim();
//                if (s.length() > 0) {
//                    fullyqualifiedTables.add(s);
//                }
//            }
//        }
//
        Set<String> contexts = new HashSet<String>();
//        if (arguments.containsKey(CONTEXT_IDS)) {
//            StringTokenizer st = new StringTokenizer(
//                    arguments.get(CONTEXT_IDS), ",");
//            while (st.hasMoreTokens()) {
//                String s = st.nextToken().trim();
//                if (s.length() > 0) {
//                    contexts.add(s);
//                }
//            }
//        }

        try {
//            ConfigurationParser cp = new ConfigurationParser(warnings);
//            Configuration config = cp.parseConfiguration(configurationFile);

            DefaultShellCallback shellCallback = new DefaultShellCallback(true);

            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);

            ProgressCallback progressCallback = new VerboseProgressCallback();

            myBatisGenerator.generate(event, progressCallback, contexts, fullyqualifiedTables);

        } catch (Exception e) {
            e.printStackTrace();
            writeLine(getString("Progress.3"));
            writeLine();
//            for (String error : e.getErrors()) {
//                writeLine(error);
//            }
            return;
        }

        for (String warning : warnings) {
            writeLine(warning);
        }

        if (warnings.size() == 0) {
            writeLine(getString("Progress.4"));
        } else {
            writeLine();
            writeLine(getString("Progress.5"));
        }
    }

    private static void usage() {
        String lines = getString("Usage.Lines");
        int iLines = Integer.parseInt(lines);
        for (int i = 0; i < iLines; i++) {
            String key = "Usage." + i;
            writeLine(getString(key));
        }
    }

    private static void writeLine(String message) {
        System.out.println(message);
    }

    private static void writeLine() {
        System.out.println();
    }

    private static Map<String, String> parseCommandLine(String[] args) {
        List<String> errors = new ArrayList<String>();
        Map<String, String> arguments = new HashMap<String, String>();

        for (int i = 0; i < args.length; i++) {
            if (CONFIG_FILE.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(CONFIG_FILE, args[i + 1]);
                } else {
                    errors.add(getString(
                            "RuntimeError.19", CONFIG_FILE));
                }
                i++;
            } else if (OVERWRITE.equalsIgnoreCase(args[i])) {
                arguments.put(OVERWRITE, "Y");
            } else if (VERBOSE.equalsIgnoreCase(args[i])) {
                arguments.put(VERBOSE, "Y");
            } else if (HELP_1.equalsIgnoreCase(args[i])) {
                arguments.put(HELP_1, "Y");
            } else if (HELP_2.equalsIgnoreCase(args[i])) {
                // put HELP_1 in the map here too - so we only
                // have to check for one entry in the mainline
                arguments.put(HELP_1, "Y");
            } else if (FORCE_JAVA_LOGGING.equalsIgnoreCase(args[i])) {
                LogFactory.forceJavaLogging();
            } else if (CONTEXT_IDS.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(CONTEXT_IDS, args[i + 1]);
                } else {
                    errors.add(getString(
                            "RuntimeError.19", CONTEXT_IDS));
                }
                i++;
            } else if (TABLES.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(TABLES, args[i + 1]);
                } else {
                    errors.add(getString("RuntimeError.19", TABLES));
                }
                i++;
            } else {
                errors.add(getString("RuntimeError.20", args[i]));
            }
        }

        if (!errors.isEmpty()) {
            for (String error : errors) {
                writeLine(error);
            }

            System.exit(-1);
        }

        return arguments;
    }
}
