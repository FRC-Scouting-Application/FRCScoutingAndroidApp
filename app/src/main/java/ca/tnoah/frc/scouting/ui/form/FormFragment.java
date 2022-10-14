package ca.tnoah.frc.scouting.ui.form;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.models.template.TemplateSerializer;

public class FormFragment extends Fragment {

    public FormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TemplateSerializer.deserialize("<Scout name=\"Test Template\" type=\"Pit\" year=\"2020\" >\n" +
                "    <Section>\n" +
                "        <Header>Scout Info</Header>\n" +
                "        <Fields>\n" +
                "            <TextField label=\"Scout Name\" />\n" +
                "            <TextField label=\"Match Number\" />\n" +
                "        </Fields>\n" +
                "    </Section>\n" +
                "    <Section>\n" +
                "        <Header>Drivetrain</Header>\n" +
                "        <Fields>\n" +
                "            <RadioList label=\"Drivetrain Type\" other=\"true\">\n" +
                "                <Item label=\"4 Wheel Tank\" />\n" +
                "                <Item label=\"6 Wheel Tank\" />\n" +
                "                <Item label=\"Mecanum\" />\n" +
                "                <Item label=\"Swerve\" />\n" +
                "            </RadioList>\n" +
                "        </Fields>\n" +
                "    </Section>\n" +
                "    <Section>\n" +
                "        <Header>Auto</Header>\n" +
                "        <Fields>\n" +
                "            <CheckList label=\"Ports during auto\">\n" +
                "                <Item label=\"Lower\" />\n" +
                "                <Item label=\"High\" />\n" +
                "                <Item label=\"Inner\" />\n" +
                "            </CheckList>\n" +
                "            <Counter label=\"How many fuel cells does your robot start with?\" max=\"5\" />\n" +
                "            <Checkbox label=\"Can it cross the baseline?\" />\n" +
                "            <TextField label=\"Robot Weight\" />\n" +
                "        </Fields>\n" +
                "    </Section>\n" +
                "</Scout>");

        return inflater.inflate(R.layout.fragment_form, container, false);
    }

}