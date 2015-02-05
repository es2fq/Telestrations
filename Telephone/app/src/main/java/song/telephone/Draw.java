package song.telephone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class Draw extends Activity implements OnClickListener {

    private DrawingView drawView;
    private ImageButton currPaint , drawBtn , eraseBtn , newBtn , saveBtn , checkBtn;
    private float smallBrush , mediumBrush , largeBrush;

    TextView word;
    String drawWord;

    BluetoothAdapter ba;
    Set<BluetoothDevice> pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        word = (TextView)findViewById(R.id.word);

        Intent intent = getIntent();
        drawWord = intent.getStringExtra("word");

        word.setText( drawWord );

        //////////////////////////////////////////////////////////////////////

        drawView = (DrawingView)findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable( getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush  = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush  = getResources().getInteger(R.integer.large_size);

        drawView.setBrushSize( mediumBrush );

        drawBtn  = (ImageButton)findViewById(R.id.draw_btn);
//        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        newBtn   = (ImageButton)findViewById(R.id.new_btn);
        saveBtn  = (ImageButton)findViewById(R.id.save_btn);
        checkBtn = (ImageButton)findViewById(R.id.check_btn);

        drawBtn.setOnClickListener( this );
//        eraseBtn.setOnClickListener( this );
        newBtn.setOnClickListener( this );
        saveBtn.setOnClickListener( this );
        checkBtn.setOnClickListener( this );

        //////////////////////////////////////////////////////////////////////
    }

    public void onClick( View view )
    {
        if( view.getId() == R.id.draw_btn ) {
            final Dialog brushDialog = new Dialog( this );
            brushDialog.setTitle( "Brush size:" );
            brushDialog.setContentView( R.layout.brush_chooser );

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener( new OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawView.setBrushSize( smallBrush );
                    drawView.setLastBrushSize( smallBrush );
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener( new OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawView.setBrushSize( mediumBrush );
                    drawView.setLastBrushSize( mediumBrush );
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener( new OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawView.setBrushSize( largeBrush );
                    drawView.setLastBrushSize( largeBrush );
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }

//        else if( view.getId() == R.id.erase_btn ) {
//            final Dialog brushDialog = new Dialog( this );
//            brushDialog.setTitle( "Eraser size:" );
//            brushDialog.setContentView( R.layout.brush_chooser );
//
//            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
//            smallBtn.setOnClickListener( new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    drawView.setErase( true );
//                    drawView.setBrushSize( smallBrush );
//                    drawView.setErase( false );
//                    brushDialog.dismiss();
//                }
//            });
//
//            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
//            mediumBtn.setOnClickListener( new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    drawView.setErase( true );
//                    drawView.setBrushSize( mediumBrush );
//                    drawView.setErase( false );
//                    brushDialog.dismiss();
//                }
//            });
//
//            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
//            largeBtn.setOnClickListener( new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    drawView.setErase( true );
//                    drawView.setBrushSize( largeBrush );
//                    drawView.setErase( false );
//                    brushDialog.dismiss();
//                }
//            });
//
//            brushDialog.show();
//        }

        else if( view.getId() == R.id.new_btn ) {
            AlertDialog.Builder newDialog = new AlertDialog.Builder( this );

            newDialog.setTitle( "New drawing" );
            newDialog.setMessage( "Start new drawing? (You will lose the current drawing)");

            newDialog.setPositiveButton( "Yes" , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog , int i) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });

            newDialog.setNegativeButton( "No" , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog , int i) {
                    dialog.cancel();
                }
            });

            newDialog.show();
        }

        else if( view.getId() == R.id.save_btn ) {
            AlertDialog.Builder saveDialog = new AlertDialog.Builder( this );
            saveDialog.setTitle( "Save drawing" );
            saveDialog.setMessage( "Save drawing to Gallery?" );
            saveDialog.setPositiveButton( "Yes" , new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog , int i) {
                    drawView.setDrawingCacheEnabled( true );
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver() , drawView.getDrawingCache() ,
                            UUID.randomUUID().toString() + ".png" , "drawing" );
                    if( imgSaved != null ) {
                        Toast savedToast = Toast.makeText( getApplicationContext() ,
                                "Drawing saved to Gallery!" , Toast.LENGTH_SHORT );
                        savedToast.show();
                    }
                    else {
                        Toast unsavedToast = Toast.makeText( getApplicationContext() ,
                                "Uh oh! Image could not be saved." , Toast.LENGTH_SHORT );
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton( "Cancel" , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog , int i) {
                    dialog.cancel();
                }
            });

            saveDialog.show();
        }

        else if( view.getId() == R.id.check_btn ) {
            AlertDialog.Builder finishDialog = new AlertDialog.Builder( this );
            finishDialog.setTitle( "Finish drawing" );
            finishDialog.setMessage( "Are you finished drawing?" );
            finishDialog.setPositiveButton( "Yes" , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    Bitmap bitmap = drawView.getBitmap();

                    try {
                        String fileName = "bitmap.png";
                        FileOutputStream stream = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        stream.close();
                        bitmap.recycle();

                        Bundle extras = new Bundle();
                        extras.putString( "image" , fileName );
                        extras.putString( "word"  , drawWord );

//                        Intent intent = new Intent( getApplicationContext() , Name.class );
//                        intent.putExtra( "image" , fileName );
//                        startActivity( intent );

                        Intent intent = new Intent( getApplicationContext() , Name.class );
                        intent.putExtras( extras );
                        startActivity( intent );
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            finishDialog.setNegativeButton( "No" , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });

            finishDialog.show();
        }
    }

    public void paintClicked( View view ) {
        drawView.setErase( false );
        drawView.setBrushSize( drawView.getLastBrushSize() );
        if( view != currPaint ) {
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor( color );

            imgView.setImageDrawable( getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton)view;
        }
    }

    @Override
    public void onBackPressed() {

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
}
