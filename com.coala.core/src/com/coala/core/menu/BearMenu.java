package com.coala.core.menu;

import com.coala.core.utils.ExternalUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;

import java.io.IOException;

public class BearMenu extends ContributionItem {

  public BearMenu() {
  }

  public BearMenu(String id) {
    super(id);
  }

  @Override
  public void fill(Menu menu, int index) {
    String[] bears = getBears();

    for (final String bear : bears) {
      MenuItem menuItem = new MenuItem(menu, SWT.CHECK, index);
      menuItem.setText(bear);
      menuItem.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent event) {
          IFile file = (IFile) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
              .getActivePart().getSite().getPage().getActiveEditor().getEditorInput()
              .getAdapter(IFile.class);
          try {
            ExternalUtils.runBearOnFile(file, bear);
          } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
          }
        }
      });
    }
  }

  private String[] getBears() {
    // TODO: Use coala-json to fetch list of bears
    String[] bears = {"AlexBear",
                      "AnnotationBear",
                      "BootLintBear",
                      "CMakeLintBear",
                      "CPDBear",
                      "CPPCheckBear",
                      "CPPCleanBear",
                      "CPPLintBear",
                      "CSSAutoPrefixBear",
                      "CSSLintBear",
                      "CSharpLintBear",
                      "CheckstyleBear",
                      "ClangASTPrintBear",
                      "ClangBear",
                      "ClangCloneDetectionBear",
                      "ClangComplexityBear",
                      "ClangFunctionDifferenceBear",
                      "CoffeeLintBear",
                      "DartLintBear",
                      "DockerfileLintBear",
                      "ESLintBear",
                      "FormatRBear",
                      "GNUIndentBear",
                      "GitCommitBear",
                      "GoImportsBear",
                      "GoLintBear",
                      "GoReturnsBear",
                      "GoTypeBear",
                      "GoVetBear",
                      "GofmtBear",
                      "HTMLLintBear",
                      "HaskellLintBear",
                      "InferBear",
                      "InvalidLinkBear",
                      "JSComplexityBear",
                      "JSHintBear",
                      "JSONFormatBear",
                      "JavaPMDBear",
                      "JuliaLintBear",
                      "KeywordBear",
                      "LanguageToolBear",
                      "LatexLintBear",
                      "LineCountBear",
                      "LineLengthBear",
                      "LuaLintBear",
                      "MarkdownBear",
                      "MatlabIndentationBear",
                      "PEP8Bear",
                      "PHPLintBear",
                      "PerlCriticBear",
                      "ProseLintBear",
                      "PyCommentedCodeBear",
                      "PyDocStyleBear",
                      "PyImportSortBear",
                      "PyLintBear",
                      "PyUnusedCodeBear",
                      "RLintBear",
                      "RadonBear",
                      "RuboCopBear",
                      "RubySyntaxBear",
                      "SCSSLintBear",
                      "SQLintBear",
                      "ScalaLintBear",
                      "ShellCheckBear",
                      "SpaceConsistencyBear",
                      "TSLintBear",
                      "VHDLLintBear",
                      "VerilogLintBear",
                      "VintBear",
                      "XMLBear",
                      "YAMLLintBear",
                      "reSTLintBear"};
    return bears;
  }
}
  