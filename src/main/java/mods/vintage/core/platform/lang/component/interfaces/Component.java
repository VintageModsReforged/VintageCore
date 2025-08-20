package mods.vintage.core.platform.lang.component.interfaces;

import mods.vintage.core.platform.lang.component.contents.ComponentContents;
import mods.vintage.core.platform.lang.component.style.Style;

import java.util.List;

public interface Component extends Message, FormattedText {

    Style getStyle();
    ComponentContents getContents();
    List<Component> getSiblings();
    FormattedCharSequence getVisualOrderText();
}
