package com.demonwav.mcdev.insight;

import com.demonwav.mcdev.MinecraftSettings;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.MergeableLineMarkerInfo;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.util.Function;
import com.intellij.util.FunctionUtil;
import com.intellij.util.ui.ColorIcon;
import com.intellij.util.ui.TwoColorsIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import javax.swing.Icon;

public class ColorLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        if (!MinecraftSettings.getInstance().isShowChatColorGutterIcons()) {
            return null;
        }

        return ColorUtil.findColorFromElement(element, entry -> new ColorInfo(element, entry.getValue()));
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
    }

    private static class ColorInfo extends MergeableLineMarkerInfo<PsiElement> {

        private final Color color;

        public ColorInfo(@NotNull final PsiElement element, @NotNull final Color color) {
            super(
                    element,
                    element.getTextRange(),
                    new ColorIcon(12, color),
                    Pass.UPDATE_ALL,
                    FunctionUtil.<Object, String>nullConstant(),
                    null,
                    GutterIconRenderer.Alignment.CENTER
            );
            this.color = color;
        }

        @Override
        public boolean canMergeWith(@NotNull MergeableLineMarkerInfo<?> info) {
            return info instanceof ColorInfo;
        }

        @Override
        public Icon getCommonIcon(@NotNull List<MergeableLineMarkerInfo> infos) {
            if (infos.size() == 2 && infos.get(0) instanceof ColorInfo && infos.get(1) instanceof ColorInfo) {
                return new TwoColorsIcon(12, ((ColorInfo) infos.get(0)).color, ((ColorInfo) infos.get(1)).color);
            }
            return AllIcons.Gutter.Colors;
        }

        @NotNull
        @Override
        public Function<? super PsiElement, String> getCommonTooltip(@NotNull List<MergeableLineMarkerInfo> infos) {
            return FunctionUtil.nullConstant();
        }
    }
}
