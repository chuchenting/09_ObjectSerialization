package com.example.objectserialization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private EditText name_editText;
    private EditText phone_editText;
    private EditText email_editText;
    private Button add_button;
    private Button modify_button;
    private Button del_button;
    private ListView person_listView;

    private ArrayList<Person> person_list;
    private ArrayAdapter<Person> array_adapter;
    private int selected_item;

    private AlertDialog.Builder builder;
    private AlertDialog.Builder builder2;
    private final String file_name = "person_list.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name_editText = (EditText)findViewById(R.id.name_editView);
        phone_editText = (EditText)findViewById(R.id.phone_editView);
        email_editText = (EditText)findViewById(R.id.email_editView);
        add_button = (Button)findViewById(R.id.add_button);
        modify_button = (Button)findViewById(R.id.modify_button);
        del_button = (Button)findViewById(R.id.del_button);
        person_listView = (ListView) findViewById(R.id.person_listView);

        person_list = new ArrayList<Person>();
        load();
        array_adapter = new ArrayAdapter<Person>(this, R.layout.list_item, person_list);

        person_listView.setAdapter(array_adapter);
        array_adapter.notifyDataSetChanged();


        OnItemClickHandler OICH = new OnItemClickHandler();
        person_listView.setOnItemClickListener(OICH);

        ButtonHandler BH = new ButtonHandler();
        add_button.setOnClickListener(BH);
        modify_button.setOnClickListener(BH);
        del_button.setOnClickListener(BH);

        builder = new AlertDialog.Builder(MainActivity.this);
        builder2 = new AlertDialog.Builder(MainActivity.this);
    }

    public class OnItemClickHandler implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?>adapterView, View view, int position, long id){
            Person p = person_list.get((int)id);

            name_editText.setText(p.get_name());
            phone_editText.setText(p.get_phone());
            email_editText.setText(p.get_email());
            selected_item = (int)id;
        }
    }
    public class ButtonHandler implements View.OnClickListener{
        public void onClick(View view){
            if(view == add_button){
                String name = name_editText.getText().toString();
                String phone = phone_editText.getText().toString();
                String email = email_editText.getText().toString();

                if(name.equals("")||phone.equals("")){
                    builder2.setTitle("Missing Message");
                    builder2.setMessage("name and phone are required");
                    AlertDialog dialog = builder2.create();
                    dialog.show();
                    return;
                }
                Person p = new Person(name, phone, email);
                person_list.add(p);
                array_adapter.notifyDataSetChanged();

                clearEditText();

                selected_item = -1;
                save();
            }else if(view == modify_button){
                if(selected_item != -1){
                    Person p = person_list.get(selected_item);
                    String name = name_editText.getText().toString();
                    String phone = phone_editText.getText().toString();
                    String email = email_editText.getText().toString();

                    p.set_name(name);
                    p.set_phone(phone);
                    p.set_email(email);

                    array_adapter.notifyDataSetChanged();

                    clearEditText();

                    selected_item = -1;
                    save();
                }
            }else if(view == del_button){
                if(selected_item != -1){
                    builder.setTitle("Confirm Message");
                    builder.setMessage("Are you sure to del this person?");

                    DelButtonHandler handler = new DelButtonHandler();

                    builder.setPositiveButton("ok", handler);
                    builder.setNegativeButton("cancel",null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        }
    }
    private void clearEditText(){
        name_editText.setText("");
        phone_editText.setText("");
        email_editText.setText("");
    }
    private class DelButtonHandler implements DialogInterface.OnClickListener{
        public void onClick(DialogInterface dialog, int which) {
            person_list.remove(selected_item);

            array_adapter.notifyDataSetChanged();

            clearEditText();

            selected_item = -1;
            save();
        }
    }
    private void load(){
        try{
            File directory = getExternalFilesDir(null);
            File file = new File(directory, file_name);
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));

            Person p = (Person) input.readObject();
            while (p != null){
                person_list.add(p);
                p = (Person) input.readObject();
            }
            if(input != null)
                input.close();
        }catch (Exception e){
            System.out.printf("%s", e.getMessage());
        }
    }
    private void save(){
        try{
            File directory = getExternalFilesDir(null);
            File file = new File(directory, file_name);
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));

            for(int i = 0; i < person_list.size(); i++)
                output.writeObject((Person)person_list.get(i));
            if(output != null)
                output.close();
        }catch (Exception e){
            System.out.printf("error: %s\n", e.getMessage());
        }
    }
}