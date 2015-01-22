package song.telephone;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class Roulette extends Activity {

    TextView word , word2 , word3 , word4 , word5;
    //    Button button , button2;
    String drawWord;

    int wait = 0 , stop = 0;
    int count = 0 , count2 = 0;
    int num = 0;

    List<TextView> textArray = new ArrayList<TextView>();
    List<String> wordArray = new ArrayList<String>();

    Handler handler , handler2;
    Runnable runnable ,  runnable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        word  = (TextView)findViewById(R.id.word);
        word2 = (TextView)findViewById(R.id.word2);
        word3 = (TextView)findViewById(R.id.word3);
        word4 = (TextView)findViewById(R.id.word4);
        word5 = (TextView)findViewById(R.id.word5);

        getWords();

        textArray.add( word );
        textArray.add( word2 );
        textArray.add( word3 );
        textArray.add( word4 );
        textArray.add( word5 );

        selectWord();

//        button  = (Button)findViewById(R.id.button);
//        button2 = (Button)findViewById(R.id.button2);

//        button.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent( getApplicationContext() , Draw.class );
//                intent.putExtra( "word" , drawWord );
//                startActivity(intent);
//            }
//        });

//        button2.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getWords();
//            }
//        });
    }

    private void selectWord() {
        Random rand = new Random();
        num = rand.nextInt( 4 ) + 2;

        stop = rand.nextInt( 200 ) + 400;

        textArray.get(count).setBackgroundColor(Color.YELLOW);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                highlightWord();

                if( wait >= stop ) {
                    flashWord();
                    handler.removeCallbacks( runnable );
                }
                else {
                    if( wait >= 100 ) {
                        num += 4;
                    }
                    wait += num;

                    handler.postDelayed(this, wait);
                }
            }
        };
        runnable.run();
    }

    private void highlightWord() {
        if( count >= 4 ) {
            textArray.get(count).setBackgroundColor(Color.WHITE);
            count = 0;
        }
        else {
            textArray.get(count).setBackgroundColor(Color.WHITE);
            count++;
        }
        textArray.get(count).setBackgroundColor(Color.YELLOW);
    }

    private void flashWord() {
        handler2 = new Handler();
        runnable2 = new Runnable() {
            @Override
            public void run() {
                if( count2 >= 7 ) {
                    handler2.removeCallbacks( runnable2 );

                    drawWord = wordArray.get(count);

                    Intent intent = new Intent( getApplicationContext() , Draw.class );
                    intent.putExtra( "word" , drawWord );
                    startActivity(intent);
                }
                else {
                    if (count2 % 2 == 0) {
                        textArray.get(count).setBackgroundColor(Color.GREEN);
                    }
                    else {
                        textArray.get(count).setBackgroundColor(Color.WHITE);
                    }
                    count2++;
                    handler2.postDelayed(this, 500);
                }
            }
        };
        runnable2.run();
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

    private void getWords() {
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

            wordArray.add( array[a] );
            wordArray.add( array[b] );
            wordArray.add( array[c] );
            wordArray.add( array[d] );
            wordArray.add( array[e] );

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
