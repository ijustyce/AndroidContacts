package com.ijustyce.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.txh.Api.sqlite;

public class list extends baseclass {

	private TableLayout tab;
	private String table;
	private String[][] value;
	private int id = 0;
	private sqlite sql;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		table = tx.table;
		loadPerson();
	}

	private void loadPerson() {

		sql = new sqlite();
		String[] column = { "_id", "name", "phone" };
		value = sql.getData(tx.getDbFile(), "sms", "select * from "
				+ table, null, column);
		String info[][] = new String[2][3];
		int temp  = 0;

		ScrollView s = (ScrollView) findViewById(R.id.appList);

		tab = new TableLayout(this);
		tab.setStretchAllColumns(true);
		
		boolean isOdd = false;
		if(value.length % 2 != 0){
			
			isOdd = true;
		}

		for (int i = 0; i < value.length; i++) {
			for (int j = 0; j < value[0].length; j++) {

				info[temp][j] = value[i][j];
			}
			
			temp++;
			if(temp ==2){
				
				temp = 0;
				show(info , 2);
			}
		}
		if(isOdd){
			show(info , 1);
		}
		s.addView(tab);
	}

	private void show(String[][] info ,  int length) {
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int height = metric.heightPixels;
		int h = 3 * height / 16;
		int width = metric.widthPixels;
		int w = 13 * width / 30;

		TableRow row = new TableRow(this);
		for (int i = 0; i < length; i++) {
			final Button b = new Button(this);

			b.setText(info[i][1] + "\n" + info[i][2]);
			b.setWidth(w);
			b.setHeight(h);
			b.setId(id);
			id ++;
			b.setOnLongClickListener(new OnLongClickListener() {

				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub

					int id = v.getId();  //  id start from 1  
					Log.i("info", value[id][1]  + " " + value[id][2]);
					show(value[id][0] , value[id][1] , value[id][2]);
					return false;
				}

			});

			b.setBackgroundColor(getResources().getColor(R.color.appBj));
			TextView space = new TextView(this);
			space.setWidth(8);
			row.addView(space);
			row.addView(b);
		}
		TextView t = new TextView(this);
		t.setHeight(8);

		tab.addView(row);
		tab.addView(t);
	}

	public void btClick(View v) {

		switch (v.getId()) {

		case R.id.addBt:
			add();
			break;
		default:
			break;
		}
	}
	
	private void show(final String id, final String name, final String phone) {

		String sms = getResources().getString(R.string.sms).toString();
		String call = getResources().getString(R.string.call).toString();
		String copy = getResources().getString(R.string.copy).toString();
		String edit = getResources().getString(R.string.edit).toString();
		String delete = getResources().getString(R.string.delete).toString();

		String[] menu = new String[] { call, sms, copy, edit, delete };

		new AlertDialog.Builder(list.this).setTitle(name)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setItems(menu, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						// which is the id you select start from 0
						switch (which) {
						case 0:
							call(phone);
							break;
						case 1:
							sms(phone);
							break;
						case 2:
							tx.setClipboard(name + ": " + phone);
							break;
						case 3:
							edit(id , name , phone);
							break;
						default:
							break;
						case 4:
							delete(id);
							break;
						}
						dialog.dismiss();
					}
				}).show();
	}

	private void call(String phone) {

		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
		startActivity(intent);
		anim();
	}

	private void sms(String phone) {
		
		Intent intent = new Intent(Intent.ACTION_SENDTO , Uri.parse("smsto:" + phone));
		startActivity(intent);
		anim();

	}

	private void edit(String id , String name , String phone) {

		Intent intent = new Intent(this, add.class);
		intent.putExtra("name", name);
		intent.putExtra("id", id);
		intent.putExtra("phone", phone);
		startActivity(intent);
		this.finish();
	}

	public void delete(String id) {

		String [] args = {id};
		sql.delete(tx.getDbFile(), tx.table , "_id=?", args);
		reStart();
	}
	
	private void reStart(){
		
		Intent intent = new Intent(this , list.class);
		startActivity(intent);
		anim();
		this.finish();
	}

	private void add() {

		Intent intent = new Intent(this, add.class);
		startActivity(intent);
		this.finish();
	}
}
