package filechooser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import a2.marketingsms.R;

public class FileArrayAdapter extends ArrayAdapter<Option> {

    private Context ctx;
    private List<Option> options;
    private int resId;

    public FileArrayAdapter(Context context, int textViewResourceId,
                            List<Option> objects) {
        super(context, textViewResourceId, objects);
        ctx = context;
        resId = textViewResourceId;
        options = objects;
    }

    public Option getOption(int i) {
        return options.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(resId, null);
        }
        final Option o = options.get(position);
        if (o != null) {
            ImageView im = (ImageView) v.findViewById(R.id.img1);
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            TextView t2 = (TextView) v.findViewById(R.id.TextView02);

            if (o.getData().equalsIgnoreCase(ctx.getString(R.string.folder))) {
                im.setImageResource(R.drawable.folder);
            } else if (position == 0 && o.getData().equalsIgnoreCase(ctx.getString(R.string.parentDirectory))) {
                im.setImageResource(R.drawable.back);
            } else {
                String name = o.getName().toLowerCase();
                if (name.endsWith(".xls") || name.endsWith(".xlsx"))
                    im.setImageResource(R.drawable.xls);
                else if (name.endsWith(".doc") || name.endsWith(".docx"))
                    im.setImageResource(R.drawable.doc);
                else if (name.endsWith(".ppt") || o.getName().endsWith(".pptx"))
                    im.setImageResource(R.drawable.ppt);
                else if (name.endsWith(".zip"))
                    im.setImageResource(R.drawable.zip);
                else if (name.endsWith(".pdf"))
                    im.setImageResource(R.drawable.pdf);
                else if (name.endsWith(".txt"))
                    im.setImageResource(R.drawable.txt);
                else if (name.endsWith(".jpg") || name.endsWith(".jpeg"))
                    im.setImageResource(R.drawable.jpg);
                else if (name.endsWith(".csv"))
                    im.setImageResource(R.drawable.csv);
                else if (name.endsWith(".png"))
                    im.setImageResource(R.drawable.png);
                else if (name.endsWith(".apk"))
                    im.setImageResource(R.drawable.apk);
                else if (name.endsWith(".rtf"))
                    im.setImageResource(R.drawable.rtf);
                else if (name.endsWith(".gif"))
                    im.setImageResource(R.drawable.gif);
                else
                    im.setImageResource(R.drawable.document);
            }

            if (t1 != null)
                t1.setText(o.getName());
            if (t2 != null)
                t2.setText(o.getData());

        }
        return v;
    }

}
