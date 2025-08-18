package mods.vintage.core.platform.lang.component.interfaces;

import mods.vintage.core.platform.lang.component.style.Style;

public interface FormattedCharSink {
    boolean accept(int index, Style style, int width);
}
