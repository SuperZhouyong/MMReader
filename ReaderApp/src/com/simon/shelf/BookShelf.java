package com.simon.shelf;
//Download by http://www.codefans.net
import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.zlibrary.core.application.ZLApplication;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidActivity;

import com.svo.mmreader.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BookShelf extends Activity {

    private ShelfAdapter mAdapter;
    private Button shelf_image_button;
    private ListView shelf_list;
    private Button button_1 , button_2 , button_3;

    @ Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.shelf );
        init ();
        mAdapter = new ShelfAdapter ();//new adapter���������
        shelf_list.setAdapter ( mAdapter );
    }

    private void init () {
        shelf_image_button = ( Button ) findViewById ( R.id.shelf_image_button );
        shelf_list = ( ListView ) findViewById ( R.id.shelf_list );
    }

    public class ShelfAdapter extends BaseAdapter {

        int[ ] size = new int[ 10 ];//�������

        public ShelfAdapter () {

        }

        @ Override
        public int getCount () {

            if ( size.length > 3 ) {
                return size.length;
            } else {
                return 3;
            }

        }

        @ Override
        public Object getItem ( int position ) {

            return size[ position ];
        }

        @ Override
        public long getItemId ( int position ) {

            return position;
        }

        @ Override
        public View getView ( int position , View convertView , ViewGroup parent ) {
            LayoutInflater layout_inflater = ( LayoutInflater ) BookShelf.this.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
            View layout = layout_inflater.inflate ( R.layout.shelf_list_item , null );
            Button button_1 , button_2 , button_3;
            button_1 = ( Button ) layout.findViewById ( R.id.button_1 );
            button_2 = ( Button ) layout.findViewById ( R.id.button_2 );
            button_3 = ( Button ) layout.findViewById ( R.id.button_3 );

            button_1.setOnClickListener ( new ButtonOnClick () );
            button_2.setOnClickListener ( new ButtonOnClick () );
            button_3.setOnClickListener ( new ButtonOnClick () );
            return layout;
        }
    };

    public class ButtonOnClick implements OnClickListener {
        @ Override
        public void onClick ( View v ) {
            switch ( v.getId () ) {
                case R.id.button_1 :
                	String path = "file:///android_asset/data/Test.txt";
//                	String path = "/sdcard/pom.txt";
                	BookShelf.this.startActivity(new Intent(BookShelf.this,FBReader.class).putExtra(FBReader.BOOK_PATH_KEY, path));
                    Toast.makeText ( BookShelf.this , "button1" , Toast.LENGTH_SHORT ).show ();
                    break;
                case R.id.button_2 :
                    Toast.makeText ( BookShelf.this , "button2" , Toast.LENGTH_SHORT ).show ();
                    break;
                case R.id.button_3 :
                    Toast.makeText ( BookShelf.this , "button3" , Toast.LENGTH_SHORT ).show ();
                    break;
            }
        }
    }

}