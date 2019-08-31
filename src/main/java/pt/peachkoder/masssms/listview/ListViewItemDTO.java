package pt.peachkoder.masssms.listview;

public class ListViewItemDTO {

    private boolean checked;
    private String name;
    private String number;

    public ListViewItemDTO(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public ListViewItemDTO() {

    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
