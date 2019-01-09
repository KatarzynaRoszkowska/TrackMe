package pl.roszkowska.track.ui.marker;

class MarkerUIHeader extends BaseMarkerUIModel {
    final String headerText;

    protected MarkerUIHeader(long id, String headerText) {
        super(id);
        this.headerText = headerText;
    }
}
