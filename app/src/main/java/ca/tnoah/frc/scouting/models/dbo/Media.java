package ca.tnoah.frc.scouting.models.dbo;

public class Media {

    public String data;

    public byte[] getImage() {
        return data.getBytes();
    }

}
