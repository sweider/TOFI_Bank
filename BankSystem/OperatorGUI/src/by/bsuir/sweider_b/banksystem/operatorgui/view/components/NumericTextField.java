package by.bsuir.sweider_b.banksystem.operatorgui.view.components;

import javafx.scene.control.TextField;

/**
 * Created by sweid on 17.01.2016.
 */
public class NumericTextField extends TextField
{

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
        return text.matches("[0-9]*");
    }
}
