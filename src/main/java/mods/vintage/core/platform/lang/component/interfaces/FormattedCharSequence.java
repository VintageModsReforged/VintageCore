package mods.vintage.core.platform.lang.component.interfaces;

public interface FormattedCharSequence {

    FormattedCharSequence EMPTY = new FormattedCharSequence() {
        @Override
        public boolean accept(FormattedCharSink formattedCharSink) {
            return true;
        }
    };

    boolean accept(FormattedCharSink formattedCharSink);
}
