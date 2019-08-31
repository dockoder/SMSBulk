package pt.peachkoder.masssms.listview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListViewItemHolder extends RecyclerView.ViewHolder {

    private CheckBox itemCheckBox;
    private TextView itemTextViewName;
    private TextView itemTextViewNumber;

    public ListViewItemHolder(@NonNull View itemView) {
        super(itemView);
    }

    public CheckBox getItemCheckBox() {
        return itemCheckBox;
    }

    public void setItemCheckBox(CheckBox itemCheckBox) {
        this.itemCheckBox = itemCheckBox;
    }

    public TextView getItemTextViewName() {
        return itemTextViewName;
    }

    public void setItemTextViewName(TextView itemTextViewName) {
        this.itemTextViewName = itemTextViewName;
    }

    public TextView getItemTextViewNumber() {
        return itemTextViewNumber;
    }

    public void setItemTextViewNumber(TextView itemTextViewNumber) {
        this.itemTextViewNumber = itemTextViewNumber;
    }


}
