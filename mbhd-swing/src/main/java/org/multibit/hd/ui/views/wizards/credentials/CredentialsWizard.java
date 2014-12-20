package org.multibit.hd.ui.views.wizards.credentials;

import com.google.common.base.Optional;
import org.multibit.hd.ui.views.wizards.AbstractHardwareWalletWizard;
import org.multibit.hd.ui.views.wizards.AbstractWizardPanelView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Map;


/**
 * <p>Wizard to provide the following to UI for "credentials" wizard:</p>
 * <ol>
 * <li>Enter credentials. These could be a password, a Trezor PIN or no Trezor PIN if no credentials are required</li>
 * </ol>
 *
 * @since 0.0.1
 *
 */
public class CredentialsWizard extends AbstractHardwareWalletWizard<CredentialsWizardModel> {

  public CredentialsWizard(CredentialsWizardModel model, boolean isExiting) {
    super(model, isExiting, Optional.absent());
  }

  @Override
  protected void populateWizardViewMap(Map<String, AbstractWizardPanelView> wizardViewMap) {

    wizardViewMap.put(
      CredentialsState.CREDENTIALS_ENTER_PASSWORD.name(),
      new CredentialsEnterPasswordPanelView(this, CredentialsState.CREDENTIALS_ENTER_PASSWORD.name()));

    wizardViewMap.put(
      CredentialsState.CREDENTIALS_REQUEST_CIPHER_KEY.name(),
      new CredentialsRequestCipherKeyPanelView(this, CredentialsState.CREDENTIALS_REQUEST_CIPHER_KEY.name()));

    wizardViewMap.put(
      CredentialsState.CREDENTIALS_ENTER_PIN.name(),
      new CredentialsEnterPinPanelView(this, CredentialsState.CREDENTIALS_ENTER_PIN.name()));

    wizardViewMap.put(
      CredentialsState.CREDENTIALS_PRESS_CONFIRM_FOR_UNLOCK.name(),
      new CredentialsConfirmCipherKeyPanelView(this, CredentialsState.CREDENTIALS_PRESS_CONFIRM_FOR_UNLOCK.name()));

    // Transition panel that is never shown
    wizardViewMap.put(
      CredentialsState.CREDENTIALS_RESTORE.name(),
      new CredentialsRestorePanelView(this, CredentialsState.CREDENTIALS_RESTORE.name()));
  }

  @Override
  public <P> Action getRestoreAction(AbstractWizardPanelView<CredentialsWizardModel, P> wizardView) {

    return new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {

        // Ensure we cancel any ongoing hardware wallet operations
        getWizardModel().requestCancel();

        // The UI will lock up during handover so prevent further events
        JButton source = (JButton) e.getSource();
        source.setEnabled(false);

        // Since #17 all restore work is done by the welcome wizard
        // See MainController for the hand over code
        hide(CredentialsState.CREDENTIALS_RESTORE.name(), false);

      }
    };

  }
}
