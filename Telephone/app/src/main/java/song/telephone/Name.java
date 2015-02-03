package song.telephone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Name extends Activity {

    ImageView imageView;
    ExtractEditText input;
    Button button;
    String str , word;
    AlertDialog.Builder nameDialog , answerDialog , againDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.button);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        word = extras.getString( "word" );
        String fileName = extras.getString( "image" );

        Bitmap bitmap = null;
//        String fileName = getIntent().getStringExtra( "image" );
        try {
            FileInputStream input = this.openFileInput( fileName );
            bitmap = BitmapFactory.decodeStream( input );
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap( bitmap );

        //////////////////////////////////////////////

        input = (ExtractEditText)findViewById(R.id.input);

        nameDialog = new AlertDialog.Builder( this );
        answerDialog = new AlertDialog.Builder( this );
        againDialog = new AlertDialog.Builder( this );

        input.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if( keyEvent.getAction() == KeyEvent.ACTION_DOWN ) {
                    switch( i ) {
                        case KeyEvent.KEYCODE_ENTER:
                            confirmation();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation();
            }
        });
    }

    private void confirmation() {
        str = input.getText().toString();
        if( !str.equals( "" ) ) {
            nameDialog.setTitle("Naming");
            nameDialog.setMessage("Is this your final answer?");
            nameDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
//                    Intent intent = new Intent(getApplicationContext(), Draw.class);
//                    intent.putExtra( "word", str );
//                    startActivity(intent);
                    checkAnswer();
                }
            });
            nameDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });

            nameDialog.show();
        }
    }

    private void checkAnswer() {
        String lowerWord = word.replaceAll( "\\s" , "" );
        lowerWord = lowerWord.toLowerCase();
        String lowerStr = str.replaceAll( "\\s" , "" );
        lowerStr = lowerStr.toLowerCase();

        if( lowerStr.equals( lowerWord )) {
            answerDialog.setTitle( "Yay!" );
            answerDialog.setMessage( "You got it!" );
        }
        else {
            answerDialog.setTitle( "Darn!" );
            answerDialog.setMessage( "The word was [" + word + "]" );
        }

        answerDialog.setPositiveButton( "OK" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                playAgain();
            }
        });

        answerDialog.show();
    }

    private void playAgain() {
        againDialog.setTitle( "Play Again?" );
        againDialog.setMessage("Do you want to play again?");
        againDialog.setPositiveButton( "Yes" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity( new Intent( getApplicationContext() , Roulette.class ));
            }
        });
        againDialog.setNegativeButton( "No" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity( new Intent( getApplicationContext() , MyActivity.class ));
            }
        });

        againDialog.show();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.name, menu);
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
}
