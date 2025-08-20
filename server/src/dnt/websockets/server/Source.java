package dnt.websockets.server;

import java.net.URI;
import java.util.Locale;

public enum Source
{
    SOURCE1,
    UNKNOWN;

    public static Source getSource(URI uri)
    {
        String path = uri.getPath();
        int i = path.lastIndexOf('/');
        if (i < 0) return UNKNOWN;
        return valueOf(path.substring(i + 1).toUpperCase(Locale.ROOT));
    }
}
