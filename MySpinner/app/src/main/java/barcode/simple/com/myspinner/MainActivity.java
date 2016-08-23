package barcode.simple.com.myspinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import barcode.simple.com.myspinner.model.Bank;
import barcode.simple.com.myspinner.widget.CustomSpinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomSpinner<Bank> bankCustomSpinner = (CustomSpinner) findViewById(R.id.my_spinner);
        bankCustomSpinner.setDropDownListItems(getBanks());
        bankCustomSpinner.setItemClickListener(new CustomSpinner.ItemClickListener<Bank>() {
            @Override
            public void onItemSelected(Bank selectedItem) {
                Toast.makeText(getApplicationContext(), selectedItem.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ArrayList<Bank> getBanks() {
        ArrayList<Bank> banks = new ArrayList<>();
        banks.add(new Bank("Canara Bank", "CNRB00006"));
        banks.add(new Bank("Axis Bank", "AXIS00006"));
        banks.add(new Bank("Federal Bank", "FDRL00006"));
        banks.add(new Bank("State Bank", "SBI00006"));
        banks.add(new Bank("South Indian Bank", "SIB00006"));
        return banks;
    }
}
