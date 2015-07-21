package jp.android.myapp.googlemaps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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

        if (id == R.id.action_add){
            Intent regi_intent = new Intent(ListActivity.this,RegisterActivity.class);
            startActivity(regi_intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private ListView myListView;
    private SQLiteDatabase db;

    public void onStart() {
        super.onStart();

        myListView = (ListView) findViewById(R.id.listView);
        final MyDbHelper myDbHelper = new MyDbHelper(ListActivity.this);
        db = myDbHelper.getWritableDatabase();

        //RegisterActivityからのintent取得
        Intent registerIntent = getIntent();
        final String[] regData = new String[5];
        if (registerIntent != null) {
            regData[0] = registerIntent.getStringExtra("name");
            regData[1] = registerIntent.getStringExtra("addr");
            regData[2] = registerIntent.getStringExtra("tel");
            regData[3] = registerIntent.getStringExtra("kind");
            regData[4] = registerIntent.getStringExtra("date");
        }

        try {
            //データベースへの登録
            insertToDB(regData);
            //データベースから値を取得
            Cursor c = searchToDB();


            //アダプターの設定
            String[] from = {myDbHelper.NAME, myDbHelper.ADDR, myDbHelper.TEL, myDbHelper.KIND, myDbHelper.DATE};
            int[] to = {R.id.list_name, R.id.list_addr, R.id.list_tel, R.id.list_kind, R.id.list_date};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listitem, c, from, to, 0);
            myListView.setAdapter(adapter);

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                //アイテムがクリックされたら
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ListActivity.this);

                    TextView item = (TextView) view.findViewById(R.id.list_name);
                    final TextView add = (TextView) view.findViewById(R.id.list_addr);
                    //アラートダイアログのタイトルにlist_nameの値を表示
                    alert.setTitle(item.getText());
                    //メッセージにlist_addrの値を表示
                    alert.setMessage(add.getText().toString());


                    alert.setPositiveButton("mapに表示", new DialogInterface.OnClickListener() {
                        //PositiveButton(mapに表示)が押されたら
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //MapsActivityへのintent
                            Intent ListIntent = new Intent(ListActivity.this, MapsActivity.class);
                            //intentに登録した住所をつめる
                            ListIntent.putExtra("addr", add.getText().toString());
                            //intentの実行
                            startActivity(ListIntent);
                        }
                    });
                    alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    //アラートダイアログ実行
                    alert.show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //最後にデータベースを閉じる
            db.close();
        }
    }

    //データベースへの登録
    private void insertToDB(String[] regData) throws Exception {

        ContentValues values = new ContentValues();

        values.put(MyDbHelper.NAME, regData[0]);
        values.put(MyDbHelper.ADDR, regData[1]);
        values.put(MyDbHelper.TEL, regData[2]);
        values.put(MyDbHelper.KIND, regData[3]);
        values.put(MyDbHelper.DATE, regData[4]);

        db.insert(MyDbHelper.TABLE_NAME, null, values);

    }

    //データベースから値の取得(検索)
    private Cursor searchToDB() throws Exception {
        Cursor c = db.rawQuery("SELECT * FROM " + MyDbHelper.TABLE_NAME , null);

        return c;
    }

}
