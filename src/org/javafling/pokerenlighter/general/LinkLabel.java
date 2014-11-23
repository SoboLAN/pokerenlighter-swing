package org.javafling.pokerenlighter.general;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import javax.swing.JLabel;

/**
 *
 * @author Radu Murzea
 */
public class LinkLabel extends JLabel
{
    private String URL;
    
    private static final String HREF_START = "<a href=\"";
    private static final String HREF_CLOSED = "\">";
    private static final String HREF_END = "</a>";
    private static final String HTML = "<html>";
    private static final String HTML_END = "</html>";
    
    public LinkLabel(String URL, String text)
    {
        super(text);
        
        this.URL = URL;
        
        if (isBrowsingSupported()) {
            setText(linkify());
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new LinkMouseListener());
        }
    }

    private boolean isBrowsingSupported()
    {
        return Desktop.isDesktopSupported() ? Desktop.getDesktop().isSupported(Desktop.Action.BROWSE) : false;
    }

    private String linkify()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append(HTML)
            .append(HREF_START)
            .append(URL)
            .append(HREF_CLOSED)
            .append(getText())
            .append(HREF_END)
            .append(HTML_END);
        
        return sb.toString();
    }
    
    private class LinkMouseListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent evt)
        {
            try {
                Desktop.getDesktop().browse(new URI(URL));
            } catch (Exception ignored) {}
        }
    }
}
