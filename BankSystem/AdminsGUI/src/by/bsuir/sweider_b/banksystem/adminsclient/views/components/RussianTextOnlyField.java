package by.bsuir.sweider_b.banksystem.adminsclient.views.components;

import javafx.scene.control.TextField;

/**
 * Created by sweid on 18.01.2016.
 */
public class RussianTextOnlyField extends TextField {

    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text)
    {
        return text.matches("[А-Яа-я]*");
    }
}
