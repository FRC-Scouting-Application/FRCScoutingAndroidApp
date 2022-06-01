package ca.tnoah.frc.scouting.models;

public class Media {

    public String data;

    public byte[] getImage() {
        return data.getBytes();
    }

}
