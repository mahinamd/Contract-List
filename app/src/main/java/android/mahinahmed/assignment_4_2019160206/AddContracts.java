package android.mahinahmed.assignment_4_2019160206;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class AddContracts extends AppCompatActivity {

    private EditText etname,etemail,etphone_home,etphone_office,etadress;
    private Button btnsave;
    private ImageView image;
    private final int galery_request_code = 1;



    String existingKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contracts);


        image= findViewById(R.id.image);
        etname = findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etphone_home = findViewById(R.id.etphone_home);
        etphone_office = findViewById(R.id.etphone_office);
        etadress = findViewById(R.id.etadress);
        btnsave = findViewById(R.id.btnsave);





        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgGal =new Intent(Intent.ACTION_PICK);
                imgGal.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imgGal,galery_request_code);
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etname.getText().toString();
                String email = etemail.getText().toString();
                String phone_home = etphone_home.getText().toString();
                String phone_office = etphone_office.getText().toString();
                String address = etadress.getText().toString();
                if (name.isEmpty() || email.isEmpty() || phone_home.isEmpty() || phone_office.isEmpty() || address.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill up the form",Toast.LENGTH_SHORT).show();

                }

                else{
                    String value = name+"----"+email+"----"+phone_home+"----"+phone_office+"----"+address;
                    KeyValueDB db = new KeyValueDB(AddContracts.this);


                    if( existingKey.length() == 0 ){
                        String key = name + System.currentTimeMillis();
                        existingKey = key;
                        db.insertKeyValue(key,value);
                    }
                    else{
                        db.updateValueByKey(existingKey,value);
                    }
                    db.close();


                    String [] keys = {"action","id","semester","key","event"};
                    String [] values ={"backup","20180160190","2022-3",existingKey,value};
                    httpRequest(keys,values);

                    Intent intent = new Intent(AddContracts.this, ShowContracts.class);
                    startActivity(intent);

                }
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        {
            if (requestCode==galery_request_code)
            {
                image.setImageURI(data.getData());

            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    private void httpRequest(final String[] keys, final String[] values){
        new AsyncTask< Void, Void, String >() {
            @Override
            protected String doInBackground (Void...voids){
                List<NameValuePair> params = new ArrayList<>();
                for (int i = 0; i < keys.length ;i++){
                    params.add(new BasicNameValuePair(keys[i], values[i]));
                }

                String url = "https://muthosoft.com/univ/cse489/index.php";
                String data;
                try {

                    data = JSONPARSER.getInstance().makeHttpRequest(url, "POST",params);

                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute (String data){
                if (data != null) {

                    Toast.makeText(getApplicationContext(), "Saved Sucessfully", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}