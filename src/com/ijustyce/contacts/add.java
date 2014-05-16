package com.ijustyce.contacts;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.txh.Api.sqlite;

public class add extends list {

	private String table;
	private String id = "null" , name = "" , phone = "";
	private EditText nameEdit , phoneEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		
		table = tx.table;
		
		nameEdit = (EditText)findViewById(R.id.name);
		phoneEdit = (EditText)findViewById(R.id.phone);
		
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			
			id = bundle.getString("id");
			name = bundle.getString("name");
			phone = bundle.getString("phone");
			
			nameEdit.setText(name);
			phoneEdit.setText(phone);
			
			bundle.clear();
		}
	}
	
	public void btClick(View v) {

		switch (v.getId()) {
			
		case R.id.ok:
			addPerson();
			break;		
		default:
			break;
		}
	}
	
	private void addPerson(){
		
		String phoneString = phoneEdit.getText().toString().replaceAll(" ", "");
		
		String[] value = {nameEdit.getText().toString() , phoneString };
		String[] column = {"name" , "phone"};
		
		sqlite sql = new sqlite();
		sql.insertData(tx.getDbFile(), table, value, column);
		sql.insertData(tx.getDbFile(), "contacts", value, column);
		
		nameEdit.setText("");
		phoneEdit.setText("");
		
		if(!id.equals("null")){
			delete(id);
			this.finish();
		}
	}
}
