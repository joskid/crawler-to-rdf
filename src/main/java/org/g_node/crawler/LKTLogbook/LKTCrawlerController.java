/**
 * Copyright (c) 2015, German Neuroinformatics Node (G-Node)
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted under the terms of the BSD License. See
 * LICENSE file in the root of the Project.
 */

package org.g_node.crawler.LKTLogbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.g_node.crawler.Controller;
import org.g_node.srv.FileService;
import org.g_node.srv.RDFService;

/**
 * Command class for the LKT crawler.
 *
 * @author Michael Sonntag (sonntag@bio.lmu.de)
 */
public class LKTCrawlerController implements Controller {
    /**
     * File types that can be processed by this crawler.
     */
    private static final List<String> SUPPORTED_INPUT_FILE_TYPES = Collections.singletonList("ODS");

    /**
     * The actual crawler this class handles and provides.
     */
    private LKTLogbook crawler;

    /**
     * Constructor.
     * @param crl Instance of the {@link org.g_node.crawler.LKTLogbook.LKTLogbook} crawler.
     */
    public LKTCrawlerController(final LKTLogbook crl) {
        this.crawler = crl;
    }

    /**
     * Method returning the commandline options of the LKT crawler.
     * @param regCrawlers Set of all registered crawlers.
     * @return Available commandline options.
     */
    public final Options options(final Set<String> regCrawlers) {
        final Options options = new Options();

        final Option opHelp = new Option("h", "help", false, "Print this message");

        final Option opIn = Option.builder("i")
                .longOpt("in-file")
                .desc("Input file that's supposed to be parsed")
                .required()
                .hasArg()
                .valueSeparator()
                .build();

        final Option opOut = Option.builder("o")
                .longOpt("out-file")
                .desc(
                        String.join(
                                "", "Optional: Path and name of the output file. ",
                                "Files with the same name will be overwritten. ",
                                "Default file name uses format 'yyyyMMddHHmm_out.ttl'"
                        )
                )
                .hasArg()
                .valueSeparator()
                .build();

        final Option opFormat = Option.builder("f")
                .longOpt("out-format")
                .desc(
                        String.join(
                                "", "Optional: format of the RDF file that will be written.\n",
                                "Supported file formats: ", RDFService.RDF_FORMAT_MAP.keySet().toString(),
                                "\nDefault setting is the Turtle (TTL) format."
                        )
                )
                .hasArg()
                .valueSeparator()
                .build();

        options.addOption(opHelp);
        options.addOption(opIn);
        options.addOption(opOut);
        options.addOption(opFormat);

        return options;
    }

    /**
     * Method to parse information from an input file to an output file using
     * the LKT crawler. Handles all checks related to input file, output file and
     * file format before the parsing begins.
     * @param cmd Commandline input provided by the user
     */
    public final void run(final CommandLine cmd) {

        System.out.println("[Info] Checking input file...");
        final String inputFile = cmd.getOptionValue("i");
        if (!FileService.checkFile(inputFile)
                || !FileService.checkFileType(inputFile, LKTCrawlerController.SUPPORTED_INPUT_FILE_TYPES)) {
            return;
        }

        System.out.println("[Info] Checking output format...");
        final String outputFormat = cmd.getOptionValue("f", "TTL").toUpperCase(Locale.ENGLISH);
        if (!RDFService.RDF_FORMAT_MAP.containsKey(outputFormat)) {
            System.err.println(
                    String.join("",
                            "[Error] Unsupported output format: '", outputFormat, "'",
                            "\n Please use one of the following: ",
                            RDFService.RDF_FORMAT_MAP.keySet().toString()
                    )
            );
            return;
        }

        final String defaultOutputFile = String.join(
                "", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")),
                "_out.ttl"
        );
        final String outputFile = cmd.getOptionValue("o", defaultOutputFile);

        // TODO check if a robuster solution exists. Also check with Kay,
        // TODO if multiple backup files e.g. with a timestamp should exist.
        System.out.println("[Info] Creating backup file...");
        final String backupFile = String.join(
                "",
                inputFile.substring(0, inputFile.lastIndexOf(".")),
                "_backup.ods");
        try {
            Files.copy(Paths.get(inputFile), Paths.get(backupFile), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException exc) {
            System.err.println(String.join(" ", "[Error] creating backup file:", exc.getMessage()));
            exc.printStackTrace();
            return;
        }

        this.crawler.parseFile(inputFile, outputFile, outputFormat);
    }
}
