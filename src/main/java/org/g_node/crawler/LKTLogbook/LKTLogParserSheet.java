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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Object containing all information parsed from an ODS sheet.
 *
 * @author Michael Sonntag (sonntag@bio.lmu.de)
 */
public final class LKTLogParserSheet {

    /**
     * Pattern that all Date values have to be formatted in
     * to be accepted by this parser.
     */
    private static final String SUPPORTED_DATE_PATTERN = "dd.MM.yyyy";
    /**
     * Formatter used to test Date values
     * for the pattern {@link #SUPPORTED_DATE_PATTERN}.
     */
    private final DateTimeFormatter supportedDate = DateTimeFormatter
                                        .ofPattern(LKTLogParserSheet.SUPPORTED_DATE_PATTERN);
    /**
     * Animal ID of the current ODS sheet. Required value.
     */
    private String subjectID;
    /**
     * Sex of the animal of the current ODS sheet. Required value.
     */
    private String subjectSex;
    /**
     * Date of birth of the animal of the current ODS sheet.
     */
    private LocalDate dateOfBirth;
    /**
     * Date of withdrawal of the animal of the current ODS sheet.
     */
    private LocalDate dateOfWithdrawal;
    /**
     * Permit number for the animal of the current ODS sheet. Required value.
     */
    private String permitNumber;
    /**
     * Common species name of the animal of the current ODS sheet.
     */
    private String species;
    /**
     * Scientific species name of the animal of the current ODS sheet. Required value.
     */
    private String scientificName;

    /**
     * ArrayList containing all parsed experiment entries of the current ODS.
     */
    private ArrayList<LKTLogParserEntry> entries;

    /**
     * Constructor.
     */
    public LKTLogParserSheet() {
        this.entries = new ArrayList<>(0);
    }

    /**
     * Return the subjectID of the animal of the current ODS sheet.
     * @return See description.
     */
    public String getSubjectID() {
        return this.subjectID;
    }

    /**
     * Set the subjectID of the animal of the current ODS sheet.
     * This entry is required for a sheet to be complete and valid.
     * @param aid ID of the current animal.
     */
    public void setSubjectID(final String aid) {
        this.subjectID = aid;
    }

    /**
     * Return the sex of the animal of the current ODS sheet.
     * @return See description.
     */
    public String getSubjectSex() {
        return this.subjectSex;
    }

    /**
     * Set the sex of the animal of the current ODS sheet.
     * This entry is required for a sheet to be complete and valid.
     * @param asx Sex of the animal, has to be in format "f" or "m".
     */
    public void setSubjectSex(final String asx) {
        this.subjectSex = asx;
    }

    /**
     * Return the date of birth of the animal of the current ODS sheet.
     * @return Return the date of birth in format {@link #SUPPORTED_DATE_PATTERN}.
     */
    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
     * Set the date of birth of the animal of the current ODS sheet.
     * This entry has to be conform to the date format {@link #SUPPORTED_DATE_PATTERN}.
     * @param dob String containing the date of birth of the animal.
     * @return Error message, if the date does not conform to
     *  format {@link #SUPPORTED_DATE_PATTERN}
     */
    public String setDateOfBirth(final String dob) {
        String errMsg = "";
        try {
            this.dateOfBirth = LocalDate.parse(dob, this.supportedDate);
        } catch (final DateTimeParseException err) {
            if (dob == null || dob.isEmpty()) {
                errMsg = "Date of birth is missing";
            } else {
                errMsg = String.join(
                        "", "Invalid Date of birth format (", dob,
                        "). Please check the date and use format '", LKTLogParserSheet.SUPPORTED_DATE_PATTERN, "'"
                );
            }
        }
        return errMsg;
    }

    /**
     * Return the withdrawal date of the the animal of the current ODS sheet.
     * @return Animal withdrawal date in format {@link #SUPPORTED_DATE_PATTERN}.
     */
    public LocalDate getDateOfWithdrawal() {
        return this.dateOfWithdrawal;
    }

    /**
     * Set the withdrawal date of the animal of the current ODS sheet.
     * This entry has to be conform to the date format {@link #SUPPORTED_DATE_PATTERN}.
     * @param dow String containing the withdrawal date of the animal.
     * @return Error message, if the date does not conform to
     *  format {@link #SUPPORTED_DATE_PATTERN}
     */
    public String setDateOfWithdrawal(final String dow) {
        String errMsg = "";
        try {
            this.dateOfWithdrawal = LocalDate.parse(dow, this.supportedDate);
        } catch (final DateTimeParseException err) {
            if (dow == null || dow.isEmpty()) {
                errMsg = "Date of withdrawal is missing";
            } else {
                errMsg = String.join(
                        "", "Invalid Date of withdrawal format (", dow,
                        "). Please check the date and use format '", LKTLogParserSheet.SUPPORTED_DATE_PATTERN, "'"
                );
            }
        }
        return errMsg;
    }

    /**
     * Return the permit number of the animal of the current ODS sheet.
     * @return See description.
     */
    public String getPermitNumber() {
        return this.permitNumber;
    }

    /**
     * Set the permit number of the animal of the current ODS sheet.
     * This entry is required for a sheet to be complete and valid.
     * @param pnr Animal permit number.
     */
    public void setPermitNumber(final String pnr) {
        this.permitNumber = pnr;
    }

    /**
     * Returns the common species name of the animal of the current ODS sheet.
     * @return See description.
     */
    public String getSpecies() {
        return this.species;
    }

    /**
     * Sets the common species name of the animal of the current ODS sheet.
     * @param spc Common species animal name.
     */
    public void setSpecies(final String spc) {
        this.species = spc;
    }

    /**
     * Returns the scientific species name of the animal of the current ODS sheet.
     * @return See description.
     */
    public String getScientificName() {
        return this.scientificName;
    }

    /**
     * Sets the scientific species name of the animal of the current ODS sheet.
     * @param snm Scientific species animal name.
     */
    public void setScientificName(final String snm) {
        this.scientificName = snm;
    }

    /**
     * Returns ArrayList of parsed {@link LKTLogParserEntry} of the current ODS sheet.
     * @return Returns the {@link #entries} array.
     */
    public ArrayList<LKTLogParserEntry> getEntries() {
        return this.entries;
    }

    /**
     * Method adds an ArrayList of {@link LKTLogParserEntry} to the {@link #entries} array.
     * @param ent ArrayList of parsed {@link LKTLogParserEntry}.
     */
    public void setEntries(final ArrayList<LKTLogParserEntry> ent) {
        this.entries = ent;
    }

    /**
     * Method adds a single logbook entry to the {@link #entries} array.
     * @param entry Parsed {@link LKTLogParserEntry}.
     */
    public void addEntry(final LKTLogParserEntry entry) {
        this.entries.add(entry);
    }

    /**
     * Method to check if the current sheet contains all required information.
     * @return Validation message
     *  TODO maybe move all these checks directly to the setter methods
     *  TODO and come up with a good method how to pass the error messages to
     *  TODO the calling instance.
     */
    public ArrayList<String> isValidSheet() {

        final ArrayList<String> validationMessage = new ArrayList<>(0);

        this.checkEntry(validationMessage, this.subjectID, "Missing animal ID");
        this.checkEntry(validationMessage, this.permitNumber, "Missing permit number");
        this.checkEntry(validationMessage, this.species, "Missing species entry");

        if (this.subjectSex.isEmpty() || Objects.equals(this.subjectSex, "")) {
            validationMessage.add("Missing animal sex entry");
        } else if (!Objects.equals(this.subjectSex, "m") && !Objects.equals(this.subjectSex, "f")) {
            validationMessage.add(
                    String.join("", "Invalid animal sex (", this.getSubjectSex(), ")")
            );
        }
        return validationMessage;
    }

    /**
     * Convenience method checking required entries and adding error
     * messages if required.
     * @param messageList List of error messages.
     * @param val Value that is required to be checked.
     * @param message Error message that's added if a value is missing.
     */
    private void checkEntry(final ArrayList<String> messageList, final String val, final String message) {
        if (val.isEmpty() || Objects.equals(val, "")) {
            messageList.add(message);
        }
    }
}
