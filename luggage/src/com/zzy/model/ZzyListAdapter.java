package com.zzy.model;
import com.zzy.luggage.R;

import android.content.Context;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.ImageView;  
import android.widget.TextView;  
  
class ViewHolder {    
    public ImageView animal;  
    public TextView cn_word;  
    public TextView en_word;  
    public ImageView speaker;    
}    
  
public class ZzyListAdapter extends BaseAdapter {    
    private LayoutInflater mInflater = null;  
    public ZzyListAdapter(Context context){  
        super();  
        mInflater = (LayoutInflater) context  
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }  
  
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return 50;  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
  
        ViewHolder holder = null;    
        if (convertView == null) {    
            holder = new ViewHolder();    
            convertView = mInflater.inflate(R.layout.self_def_list, null);  
            holder.animal = (ImageView) convertView.findViewById(R.id.animal);    
            holder.cn_word = (TextView) convertView.findViewById(R.id.cn_word);    
            holder.en_word = (TextView) convertView.findViewById(R.id.en_word);    
            holder.speaker = (ImageView) convertView.findViewById(R.id.speaker);    
  
            convertView.setTag(holder);    
        } else {    
            holder = (ViewHolder) convertView.getTag();    
        }  
  
        holder.animal.setImageResource(R.drawable.ic_launcher);  
        holder.cn_word.setText("xxxxx");  
        holder.en_word.setText("ssssss");  
      //  holder.speaker.setImageResource(R.drawable.speaker);  
  
        holder.speaker.setOnClickListener(new OnClickListener(){  
  
            @Override  
            public void onClick(View v) {  
                System.out.println("Click on the speaker image on ListItem ");  
            }  
        });  
  
        return convertView;  
    }    
  
}  