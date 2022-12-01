package ca.tnoah.frc.scouting.services.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ca.tnoah.frc.scouting.helpers.SerializeEncode;
import ca.tnoah.frc.scouting.models.dbo.Event;
import ca.tnoah.frc.scouting.models.dbo.Match;
import ca.tnoah.frc.scouting.models.dbo.Scout;
import ca.tnoah.frc.scouting.models.dbo.Team;
import ca.tnoah.frc.scouting.models.dbo.Template;
import ca.tnoah.frc.scouting.services.DatabaseService;
import ca.tnoah.frc.scouting.services.localdb.AppDatabase;

public class DataPackage {
    public List<Event> events;
    public List<Match> matches;
    public List<Scout> scouts;
    public List<Team> teams;
    public List<Template> templates;

    /**
     * Create Data Package with no data
     */
    public DataPackage() {
        this.events = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.scouts = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.templates = new ArrayList<>();
    }

    /**
     * Create Data Package with data
     *
     * @param events list of events
     * @param matches list of matches
     * @param scouts list of scouts
     * @param teams list of teams
     * @param templates list of templates
     */
    public DataPackage(List<Event> events, List<Match> matches, List<Scout> scouts,
                       List<Team> teams, List<Template> templates) {
        this.events = events;
        this.matches = matches;
        this.scouts = scouts;
        this.teams = teams;
        this.templates = templates;
    }

    public static DataPackage createFromDB() {
        AppDatabase db = DatabaseService.getInstance().getDB();

        List<Event> events = db.eventsDAO().getAll();
        List<Match> matches = db.matchesDAO().getAll();
        List<Scout> scouts = db.scoutsDAO().getAll();
        List<Team> teams = db.teamsDAO().getAll();
        List<Template> templates = db.templatesDAO().getAll();

        return new DataPackage(events, matches, scouts, teams, templates);
    }

    /**
     * Package the data into a string
     *
     * @param dataPackage Data Package to package
     * @return base64 string of data, followed by checksum
     * @throws IOException if unable to open {@link ByteArrayInputStream}
     */
    public static String packageData(DataPackage dataPackage) throws IOException {
        return SerializeEncode.packData(dataPackage);
    }

    /**
     * Package the data into a string
     *
     * @param dataPackage Data Package to package
     * @return base64 byte[] of data, followed by checksum
     * @throws IOException if unable to open {@link ByteArrayInputStream}
     */
    public static byte[] packageDataBytes(DataPackage dataPackage) throws IOException {
        return SerializeEncode.packData(dataPackage).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Package this data into a string
     *
     * @return base64 string of data, followed by checksum
     * @throws IOException if unable to open {@link ByteArrayInputStream}
     */
    public String packageData() throws IOException {
        return packageData(this);
    }

    /**
     * Package this data into a string
     *
     * @return base64 byte[] of data, followed by checksum
     * @throws IOException if unable to open {@link ByteArrayInputStream}
     */
    public byte[] packageDataBytes() throws IOException {
        return packageData(this).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Unpack the data from a string
     *
     * @param packedData packed data in the form of a string
     * @return {@link DataPackage} result
     * @throws SerializeEncode.UnpackException if there was a failure in unpacking the data
     * @throws IOException if unable to open {@link ByteArrayInputStream}
     */
    public static DataPackage unpackData(String packedData) throws SerializeEncode.UnpackException, IOException {
        return SerializeEncode.unpackData(packedData, DataPackage.class);
    }

    /**
     * Unpack the data from a string in the form of a byte[]
     *
     * @param packedData packed data
     * @return {@link DataPackage} result
     * @throws SerializeEncode.UnpackException if there was a failure in unpacking the data
     * @throws IOException if unable to open {@link ByteArrayInputStream}
     */
    public static DataPackage unpackData(byte[] packedData) throws SerializeEncode.UnpackException, IOException {
        String data = new String(packedData, StandardCharsets.UTF_8);
        return SerializeEncode.unpackData(data, DataPackage.class);
    }
}