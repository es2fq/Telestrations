package song.telephone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Name extends Activity {

    ImageView imageView;
    ExtractEditText input;
    Button button;
    String str;
    AlertDialog.Builder nameDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.button);

        Bitmap bitmap = null;
        String fileName = getIntent().getStringExtra( "image" );
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
                    Intent intent = new Intent(getApplicationContext(), Draw.class);
                    intent.putExtra( "word", str );
                    startActivity(intent);
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
