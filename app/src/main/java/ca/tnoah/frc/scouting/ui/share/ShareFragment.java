package ca.tnoah.frc.scouting.ui.share;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.tnoah.frc.scouting.R;
import ca.tnoah.frc.scouting.ui.share.client.BluetoothClient;
import ca.tnoah.frc.scouting.ui.share.server.BluetoothServer;

public class ShareFragment extends Fragment {
    private static final String TAG = "==ShareFragment==";

    private EditText passphrase;

    public ShareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        passphrase = view.findViewById(R.id.btPassphraseEditText);
        ((Button) view.findViewById(R.id.btHostServerBtn)).setOnClickListener((v) -> startServer());
        ((Button) view.findViewById(R.id.btConnectToServerBtn)).setOnClickListener((v) -> startClient());

        return view;
    }

    @Nullable
    private String getPassphrase() {
        String pass = passphrase.getText().toString().trim();
        if (pass.isEmpty()) {
            Toast toast = Toast.makeText(getContext(), R.string.bt_invalid_passphrase, Toast.LENGTH_SHORT);
            toast.show();

            return null;
        }

        return pass;
    }

    private void startServer() {
        String pass = getPassphrase();
        if (pass == null) return;

        Intent startServer = new Intent(getContext(), BluetoothServer.class);
        startServer.putExtra(Bluetooth.INTENT_PASSPHRASE, pass);
        startActivity(startServer);
    }

    private void startClient() {
        String pass = getPassphrase();
        if (pass == null) return;

        Intent startClient = new Intent(getContext(), BluetoothClient.class);
        startClient.putExtra(Bluetooth.INTENT_PASSPHRASE, pass);
        startActivity(startClient);
    }

}