package jp.android.myapp.googlemaps;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        //ボタンのidを取得
        Button regi_button = (Button)findViewById(R.id.regi_button);
        //クリックされたらintentを生成、入力された値をputExtraでintentにつめて実行
        regi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(RegisterActivity.this, ListActivity.class);

                EditText name = (EditText)findViewById(R.id.name_edit);
                EditText addr = (EditText)findViewById(R.id.addr_edit);
                EditText tel = (EditText)findViewById(R.id.tel_edit);
                EditText kind = (EditText)findViewById(R.id.kind_edit);
                EditText date = (EditText)findViewById(R.id.date_edit);

                registerIntent.putExtra("name", name.getText().toString());
                registerIntent.putExtra("addr", addr.getText().toString());
                registerIntent.putExtra("tel", tel.getText().toString());
                registerIntent.putExtra("kind", kind.getText().toString());
                registerIntent.putExtra("date", date.getText().toString());

                startActivity(registerIntent);

            }
        });

        //ボタンのid取得
        Button cancel_button = (Button)findViewById(R.id.cancel_button);
        //クリックされたらListActivityへのintent生成して実行
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(RegisterActivity.this, ListActivity.class);
                startActivity(cancel);
            }
        });
    }
}