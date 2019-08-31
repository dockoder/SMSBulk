package pt.peachkoder.masssms.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import pt.peachkoder.masssms.R;

public class ListViewItemCheckboxBaseAdapter extends BaseAdapter {

    private List<ListViewItemDTO> listViewItemDTOList = null;
    private Context ctx = null;

    public  ListViewItemCheckboxBaseAdapter(List<ListViewItemDTO> listViewItemDTOList, Context ctx) {

        this.listViewItemDTOList = listViewItemDTOList;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {

        return (listViewItemDTOList==null) ? 0 : listViewItemDTOList.size();

    }

    @Override
    public Object getItem(int position) {

        return (listViewItemDTOList==null) ? null : listViewItemDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListViewItemHolder viewItemHolder;

        if(convertView!=null){

            viewItemHolder = (ListViewItemHolder) convertView.getTag();

        } else {

            convertView = View.inflate(ctx, R.layout.activity_list_view_with_checkbox_item, null);

            CheckBox listItemCheckbox = convertView.findViewById(R.id.list_view_item_checkbox);

            TextView listItemTextName =  convertView.findViewById(R.id.list_view_item_name);

            TextView listItemTextNumber = convertView.findViewById(R.id.list_view_item_number);

            viewItemHolder = new ListViewItemHolder(convertView);

            viewItemHolder.setItemCheckBox(listItemCheckbox);

            viewItemHolder.setItemTextViewName(listItemTextName);

            viewItemHolder.setItemTextViewNumber(listItemTextNumber);

            convertView.setTag(viewItemHolder);
        }


        ListViewItemDTO listViewItemDto = listViewItemDTOList.get(position);
        viewItemHolder.getItemCheckBox().setChecked(listViewItemDto.isChecked());
        viewItemHolder.getItemTextViewName().setText(listViewItemDto.getName());
        viewItemHolder.getItemTextViewNumber().setText(listViewItemDto.getNumber());

        return convertView;
    }
}
