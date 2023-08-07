package android.mahinahmed.assignment_4_2019160206;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ShowContracts extends AppCompatActivity {
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contracts);
        listView = findViewById(R.id.listviewId);
        loadData();

    }

    public void loadData() {
        ArrayList<String> list1 = new ArrayList<>();
        KeyValueDB db = new KeyValueDB(this);
        Cursor cursor = db.getAllKeyValues();
        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {
                String value = cursor.getString(1);
                String[] values = value.split("----");
                String str = "name :" + values[0] + "\nEmail :" + values[1] + "\nphone(office) :" + values[3] ;

                list1.add(str);

            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_list_items, R.id.textViewId, list1);

        listView.setAdapter(adapter);




    }
    @Override
    protected void onStart() {
        super.onStart();
        String [] keys = {"action","id","semester"};
        String [] values = {"restore","20180160190","2022-3"};
        httpRequest(keys,values);
    }


    private void httpRequest(final String keys[],final String values[]){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                for (int i=0; i<keys.length; i++){
                    params.add(new BasicNameValuePair(keys[i],values[i]));
                }
                String url= "https://www.muthosoft.com/univ/cse489/index.php";
                String data="";
                try {
                    data=JSONPARSER.getInstance().makeHttpRequest(url,"POST",params);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String data){
                if(data!=null){
                    System.out.println(data);
                    updateEventListByServerData(data);
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    public void updateEventListByServerData(String data){
        try{
            JSONObject jo = new JSONObject(data);
            if(jo.has("events")){
                JSONArray ja = jo.getJSONArray("events");
                for(int i=0; i<ja.length(); i++){
                    JSONObject event = ja.getJSONObject(i);
                    String eventKey = event.getString("e_key");
                    String eventValue = event.getString("e_value");
// split eventValue to show in event list
                }
            }
        }catch(Exception e){}
    }



}