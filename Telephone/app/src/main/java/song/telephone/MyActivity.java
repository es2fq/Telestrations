package song.telephone;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class MyActivity extends Activity {

    TextView word , word2 , word3 , word4 , word5;
    Button button , button2;
    String drawWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        word  = (TextView)findViewById(R.id.word);
        word2 = (TextView)findViewById(R.id.word2);
        word3 = (TextView)findViewById(R.id.word3);
        word4 = (TextView)findViewById(R.id.word4);
        word5 = (TextView)findViewById(R.id.word5);
        readFile();

        button  = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext() , Draw.class );
                intent.putExtra( "word" , drawWord );
                startActivity(intent);
            }
        });

        button2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFile();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readFile() {
        AssetManager assetManager = getAssets();

        try {
            InputStream input = assetManager.open( "words.txt" );

            int size = input.available();

            byte[] buffer = new byte[size];
            input.read( buffer );
            input.close();

            String text = new String( buffer );
            String[] array = text.split( "/" );

            int a , b , c , d , e;
            int[] nums;
            while( true ) {
                Random rand = new Random();
                a = rand.nextInt(205);
                b = rand.nextInt(205);
                c = rand.nextInt(205);
                d = rand.nextInt(205);
                e = rand.nextInt(205);

                nums = new int[]{ a , b , c , d , e };

                boolean dup = false;
                Set set = new HashSet();
                for( int i = 0; i < 5; i++ ) {
                    if( !set.add( nums[i] )) {
                        dup = true;
                    }
                }
                if( !dup ) {
                    break;
                }
            }
            Random rand = new Random();
            int n = rand.nextInt( 5 );

            drawWord = array[nums[n]];

            word. setText( array[a] );
            word2.setText( array[b] );
            word3.setText( array[c] );
            word4.setText( array[d] );
            word5.setText( array[e] );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
