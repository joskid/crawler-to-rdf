/**
 * Copyright (c) 2015, German Neuroinformatics Node (G-Node)
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted under the terms of the BSD License. See
 * LICENSE file in the root of the Project.
 */

package org.g_node.srv;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.log4j.Logger;

/**
 * Main service class for saving data to an RDF file.
 *
 * @author Michael Sonntag (sonntag@bio.lmu.de)
 */
public final class RDFService {

    /**
     * Map returning the RDF formats supported by this service.
     */
    public static final Map<String, RDFFormat> RDF_FORMAT_MAP =
            Collections.unmodifiableMap(new HashMap<String, RDFFormat>(3) {
                {
                    put("TTL", RDFFormat.TURTLE_PRETTY);
                    put("RDF/XML", RDFFormat.RDFXML);
                    put("NTRIPLES", RDFFormat.NTRIPLES);
                    put("JSON-LD", RDFFormat.JSONLD);
                }
            });

    /**
     * Map RDF formats to the correct file ending.
     */
    public static final Map<String, String> RDF_FORMAT_EXTENSION =
            Collections.unmodifiableMap(new HashMap<String, String>(3) {
                {
                    put("TTL", "ttl");
                    put("RDF/XML", "rdf");
                    put("NTRIPLES", "nt");
                    put("JSON-LD", "jsonld");
                }
            });

    /**
     * Access to the main LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(RDFService.class.getName());

    /**
     * Write an rdf model to an output file. This method will overwrite any
     * files with the same path and filename.
     * @param fileName Path and filename of the output file.
     * @param model RDF model that's supposed to be written to the file.
     * @param format Output format of the RDF file.
     */
    public static void writeModelToFile(final String fileName, final Model model, final String format) {

        final File file = new File(fileName);

        try {
            final FileOutputStream fos = new FileOutputStream(file);
            RDFService.LOGGER.info(
                    String.join(
                            "", "Writing data to RDF file, ",
                            fileName, " using format '", format, "'"
                    )
            );
            try {
                RDFDataMgr.write(fos, model, RDFService.RDF_FORMAT_MAP.get(format));
                fos.close();
            } catch (IOException ioExc) {
                RDFService.LOGGER.error("Error closing file stream.");
            }
        } catch (FileNotFoundException exc) {
            RDFService.LOGGER.error(
                    String.join("", "Could not open output file ", fileName)
            );
        }
    }

    /**
     * Open an RDF file, load the data and return the RDF model. Method will not check,
     * if the file is actually a valid RDF file or if the file extension matches
     * the content of the file.
     * @param fileName Path and filename of a valid RDF file.
     * @return Model created from the data within the provided RDF file.
     */
    public static Model openModelFromFile(final String fileName) {
        return RDFDataMgr.loadModel(fileName);
    }

}
