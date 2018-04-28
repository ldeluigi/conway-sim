package view.swing.sandbox;

/**
 * This class implements the management of the resize apply button and the save
 * button, extending all the functionalities of {@link GenerationPanel}.
 */
public class ExtendedGenerationPanel extends GenerationPanel {
    private static final long serialVersionUID = -4340679165012371205L;
    private final SimpleSandbox view;

    /**
     * 
     * @param view
     *            is the SimpleSandbox that calls this ExtendedGenerationPanel
     */
    public ExtendedGenerationPanel(final SimpleSandbox view) {
        super(view);
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void end() {
        super.end();
        this.view.setButtonApplyEnabled(true);
        this.view.setSaveEnable(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void start() {
        super.start();
        this.view.setButtonApplyEnabled(false);
        this.view.setSaveEnable(false);
    }
}
