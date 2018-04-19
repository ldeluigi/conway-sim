package view.swing.sandbox;

/**
 *
 */
public class ExtendedGenerationPanel extends GenerationPanel {
    private static final long serialVersionUID = -4340679165012371205L;
    private final SimpleSandbox view;

    /**
     * 
     * @param view ths SimpleSandbox that call this ExtendedGenerationPanel
     */
    public ExtendedGenerationPanel(final SimpleSandbox view) {
        super(view);
        this.view = view;
    }

    /**
     * 
     */
    @Override
    protected void end() {
        super.end();
        this.view.setButtonApplyEnabled(true);
    }

    /**
     * 
     */
    @Override
    protected void start() {
        super.start();
        this.view.setButtonApplyEnabled(false);
    }
}
