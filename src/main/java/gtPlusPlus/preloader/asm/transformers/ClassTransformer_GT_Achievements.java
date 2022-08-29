package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class ClassTransformer_GT_Achievements {

    private final boolean isValid;
    private final ClassReader reader;
    private final ClassWriter writer;
    private static boolean mDidRemoveAssLineRecipeAdder = false;

    public ClassTransformer_GT_Achievements(byte[] basicClass) {

        ClassReader aTempReader = null;
        ClassWriter aTempWriter = null;

        aTempReader = new ClassReader(basicClass);
        aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);

        aTempReader.accept(new MethodAdaptor(aTempWriter), 0);

        if (mDidRemoveAssLineRecipeAdder) {
            FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Patching GT .09");
            injectMethod(aTempWriter);
            patchOnItemPickup09(aTempWriter);
        } else {
            FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Patching GT .08");
            patchOnItemPickup08(aTempWriter);
        }

        if (aTempReader != null && aTempWriter != null) {
            isValid = true;
        } else {
            isValid = false;
        }

        FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Valid? " + isValid + ".");
        reader = aTempReader;
        writer = aTempWriter;
    }

    public boolean isValidTransformer() {
        return isValid;
    }

    public ClassReader getReader() {
        return reader;
    }

    public ClassWriter getWriter() {
        return writer;
    }

    public boolean injectMethod(ClassWriter cw) {
        MethodVisitor mv;
        boolean didInject = false;
        FMLRelaunchLog.log(
                "[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Injecting " + "registerAssAchievement" + ".");

        /**
         * Inject new, safer code
         */

        /*mv = cw.visitMethod(ACC_PUBLIC, "registerAssAchievement", "(Lgregtech/api/util/GT_Recipe;)Lnet/minecraft/stats/Achievement;", null, null);
        mv.visitCode();
        Label l0 = new Label();
        Label l1 = new Label();
        Label l2 = new Label();
        mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLineNumber(291, l3);
        mv.visitVarInsn(ALOAD, 1);
        Label l4 = new Label();
        mv.visitJumpInsn(IFNONNULL, l4);
        Label l5 = new Label();
        mv.visitLabel(l5);
        mv.visitLineNumber(292, l5);
        mv.visitLdcInsn("Someone tried to register an achievement for an invalid recipe. Please report this to Alkalus.");
        mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/api/objects/Logger", "INFO", "(Ljava/lang/String;)V", false);
        Label l6 = new Label();
        mv.visitLabel(l6);
        mv.visitLineNumber(293, l6);
        mv.visitInsn(ACONST_NULL);
        mv.visitInsn(ARETURN);
        mv.visitLabel(l4);
        mv.visitLineNumber(295, l4);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitInsn(ICONST_0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/util/GT_Recipe", "getOutput", "(I)Lnet/minecraft/item/ItemStack;", false);
        Label l7 = new Label();
        mv.visitJumpInsn(IFNONNULL, l7);
        Label l8 = new Label();
        mv.visitLabel(l8);
        mv.visitLineNumber(296, l8);
        mv.visitLdcInsn("Someone tried to register an achievement for a recipe with null output. Please report this to Alkalus.");
        mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/api/objects/Logger", "INFO", "(Ljava/lang/String;)V", false);
        Label l9 = new Label();
        mv.visitLabel(l9);
        mv.visitLineNumber(297, l9);
        mv.visitInsn(ACONST_NULL);
        mv.visitInsn(ARETURN);
        mv.visitLabel(l7);
        mv.visitLineNumber(299, l7);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitInsn(ICONST_0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/util/GT_Recipe", "getOutput", "(I)Lnet/minecraft/item/ItemStack;", false);
        mv.visitVarInsn(ASTORE, 3);
        mv.visitLabel(l0);
        mv.visitLineNumber(301, l0);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitVarInsn(ASTORE, 2);
        mv.visitLabel(l1);
        mv.visitLineNumber(302, l1);
        Label l10 = new Label();
        mv.visitJumpInsn(GOTO, l10);
        mv.visitLabel(l2);
        mv.visitLineNumber(303, l2);
        mv.visitFrame(F_FULL, 4, new Object[] {"gregtech/loaders/misc/GT_Achievements", "gregtech/api/util/GT_Recipe", TOP, "net/minecraft/item/ItemStack"}, 1, new Object[] {"java/lang/Throwable"});
        mv.visitVarInsn(ASTORE, 4);
        Label l11 = new Label();
        mv.visitLabel(l11);
        mv.visitLineNumber(304, l11);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/ItemUtils", "getUnlocalizedItemName", "(Lnet/minecraft/item/ItemStack;)Ljava/lang/String;", false);
        mv.visitVarInsn(ASTORE, 2);
        mv.visitLabel(l10);
        mv.visitLineNumber(306, l10);
        mv.visitFrame(F_FULL, 4, new Object[] {"gregtech/loaders/misc/GT_Achievements", "gregtech/api/util/GT_Recipe", "java/lang/String", "net/minecraft/item/ItemStack"}, 0, new Object[] {});
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "gregtech/loaders/misc/GT_Achievements", "achievementList", "Ljava/util/concurrent/ConcurrentHashMap;");
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/concurrent/ConcurrentHashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
        Label l12 = new Label();
        mv.visitJumpInsn(IFNONNULL, l12);
        Label l13 = new Label();
        mv.visitLabel(l13);
        mv.visitLineNumber(307, l13);
        mv.visitFieldInsn(GETSTATIC, "gregtech/loaders/misc/GT_Achievements", "assReg", "I");
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitFieldInsn(PUTSTATIC, "gregtech/loaders/misc/GT_Achievements", "assReg", "I");
        Label l14 = new Label();
        mv.visitLabel(l14);
        mv.visitLineNumber(308, l14);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitIntInsn(BIPUSH, 11);
        mv.visitFieldInsn(GETSTATIC, "gregtech/loaders/misc/GT_Achievements", "assReg", "I");
        mv.visitInsn(ICONST_5);
        mv.visitInsn(IREM);
        mv.visitInsn(IADD);
        mv.visitInsn(INEG);
        mv.visitFieldInsn(GETSTATIC, "gregtech/loaders/misc/GT_Achievements", "assReg", "I");
        mv.visitInsn(ICONST_5);
        mv.visitInsn(IDIV);
        mv.visitIntInsn(BIPUSH, 8);
        mv.visitInsn(ISUB);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitInsn(ICONST_0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/util/GT_Recipe", "getOutput", "(I)Lnet/minecraft/item/ItemStack;", false);
        mv.visitFieldInsn(GETSTATIC, "net/minecraft/stats/AchievementList", "openInventory", "Lnet/minecraft/stats/Achievement;");
        mv.visitInsn(ICONST_0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/loaders/misc/GT_Achievements", "registerAchievement", "(Ljava/lang/String;IILnet/minecraft/item/ItemStack;Lnet/minecraft/stats/Achievement;Z)Lnet/minecraft/stats/Achievement;", false);
        mv.visitInsn(ARETURN);
        mv.visitLabel(l12);
        mv.visitLineNumber(310, l12);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitInsn(ACONST_NULL);
        mv.visitInsn(ARETURN);
        Label l15 = new Label();
        mv.visitLabel(l15);
        mv.visitLocalVariable("this", "Lgregtech/loaders/misc/GT_Achievements;", null, l3, l15, 0);
        mv.visitLocalVariable("recipe", "Lgregtech/api/util/GT_Recipe;", null, l3, l15, 1);
        mv.visitLocalVariable("aSafeUnlocalName", "Ljava/lang/String;", null, l1, l2, 2);
        mv.visitLocalVariable("aSafeUnlocalName", "Ljava/lang/String;", null, l10, l15, 2);
        mv.visitLocalVariable("aStack", "Lnet/minecraft/item/ItemStack;", null, l0, l15, 3);
        mv.visitLocalVariable("t", "Ljava/lang/Throwable;", null, l11, l10, 4);
        mv.visitMaxs(7, 5);
        mv.visitEnd();*/

        // Lets just static call my replacement function
        mv = cw.visitMethod(
                ACC_PUBLIC,
                "registerAssAchievement",
                "(Lgregtech/api/util/GT_Recipe;)Lnet/minecraft/stats/Achievement;",
                null,
                null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(291, l0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "gtPlusPlus/xmod/gregtech/loaders/misc/AssLineAchievements",
                "registerAssAchievement",
                "(Lgregtech/api/util/GT_Recipe;)Lnet/minecraft/stats/Achievement;",
                false);
        mv.visitInsn(ARETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", "Lgregtech/loaders/misc/GT_Achievements;", null, l0, l1, 0);
        mv.visitLocalVariable("recipe", "Lgregtech/api/util/GT_Recipe;", null, l0, l1, 1);
        mv.visitMaxs(1, 2);
        mv.visitEnd();

        didInject = true;
        FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Method injection complete.");
        return didInject;
    }

    public boolean patchOnItemPickup08(ClassWriter cw) {
        MethodVisitor mv;
        boolean didInject = false;
        FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Injecting " + "onItemPickup" + ".");

        AnnotationVisitor av0;
        mv = cw.visitMethod(
                ACC_PUBLIC,
                "onItemPickup",
                "(Lnet/minecraftforge/event/entity/player/EntityItemPickupEvent;)V",
                null,
                null);
        av0 = mv.visitAnnotation("Lcpw/mods/fml/common/eventhandler/SubscribeEvent;", true);
        av0.visitEnd();
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(418, l0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(
                GETFIELD,
                "net/minecraftforge/event/entity/player/EntityItemPickupEvent",
                "entityPlayer",
                "Lnet/minecraft/entity/player/EntityPlayer;");
        mv.visitVarInsn(ASTORE, 2);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(419, l1);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(
                GETFIELD,
                "net/minecraftforge/event/entity/player/EntityItemPickupEvent",
                "item",
                "Lnet/minecraft/entity/item/EntityItem;");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "net/minecraft/entity/item/EntityItem",
                "getEntityItem",
                "()Lnet/minecraft/item/ItemStack;",
                false);
        mv.visitVarInsn(ASTORE, 3);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(420, l2);
        mv.visitVarInsn(ALOAD, 2);
        Label l3 = new Label();
        mv.visitJumpInsn(IFNULL, l3);
        mv.visitVarInsn(ALOAD, 3);
        Label l4 = new Label();
        mv.visitJumpInsn(IFNONNULL, l4);
        mv.visitLabel(l3);
        mv.visitLineNumber(421, l3);
        mv.visitFrame(
                F_APPEND,
                2,
                new Object[] {"net/minecraft/entity/player/EntityPlayer", "net/minecraft/item/ItemStack"},
                0,
                null);
        mv.visitInsn(RETURN);
        mv.visitLabel(l4);
        mv.visitLineNumber(424, l4);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "gregtech/api/util/GT_OreDictUnificator",
                "getItemData",
                "(Lnet/minecraft/item/ItemStack;)Lgregtech/api/objects/ItemData;",
                false);
        mv.visitVarInsn(ASTORE, 4);
        Label l5 = new Label();
        mv.visitLabel(l5);
        mv.visitLineNumber(425, l5);
        mv.visitVarInsn(ALOAD, 4);
        Label l6 = new Label();
        mv.visitJumpInsn(IFNULL, l6);
        Label l7 = new Label();
        mv.visitLabel(l7);
        mv.visitLineNumber(426, l7);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/OrePrefixes", "dust", "Lgregtech/api/enums/OrePrefixes;");
        Label l8 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l8);
        Label l9 = new Label();
        mv.visitLabel(l9);
        mv.visitLineNumber(427, l9);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/ItemData", "mMaterial", "Lgregtech/api/objects/MaterialStack;");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/Materials", "Lutetium", "Lgregtech/api/enums/Materials;");
        Label l10 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l10);
        Label l11 = new Label();
        mv.visitLabel(l11);
        mv.visitLineNumber(428, l11);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("newmetal");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        mv.visitLabel(l10);
        mv.visitLineNumber(430, l10);
        mv.visitFrame(F_APPEND, 1, new Object[] {"gregtech/api/objects/ItemData"}, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("cleandust");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l12 = new Label();
        mv.visitLabel(l12);
        mv.visitLineNumber(431, l12);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l8);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/OrePrefixes", "ore", "Lgregtech/api/enums/OrePrefixes;");
        Label l13 = new Label();
        mv.visitJumpInsn(IF_ACMPEQ, l13);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(
                GETSTATIC, "gregtech/api/enums/OrePrefixes", "oreBlackgranite", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitJumpInsn(IF_ACMPEQ, l13);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(
                GETSTATIC, "gregtech/api/enums/OrePrefixes", "oreEndstone", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitJumpInsn(IF_ACMPEQ, l13);
        Label l14 = new Label();
        mv.visitLabel(l14);
        mv.visitLineNumber(432, l14);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(
                GETSTATIC, "gregtech/api/enums/OrePrefixes", "oreNetherrack", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitJumpInsn(IF_ACMPEQ, l13);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(
                GETSTATIC, "gregtech/api/enums/OrePrefixes", "oreRedgranite", "Lgregtech/api/enums/OrePrefixes;");
        Label l15 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l15);
        mv.visitLabel(l13);
        mv.visitLineNumber(433, l13);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, 5);
        Label l16 = new Label();
        mv.visitLabel(l16);
        Label l17 = new Label();
        mv.visitJumpInsn(GOTO, l17);
        Label l18 = new Label();
        mv.visitLabel(l18);
        mv.visitLineNumber(434, l18);
        mv.visitFrame(F_APPEND, 1, new Object[] {INTEGER}, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/api/objects/ItemData",
                "getAllMaterialStacks",
                "()Ljava/util/ArrayList;",
                false);
        mv.visitVarInsn(ILOAD, 5);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;", false);
        mv.visitTypeInsn(CHECKCAST, "gregtech/api/objects/MaterialStack");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/enums/Materials", "name", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l19 = new Label();
        mv.visitLabel(l19);
        mv.visitLineNumber(435, l19);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/api/objects/ItemData",
                "getAllMaterialStacks",
                "()Ljava/util/ArrayList;",
                false);
        mv.visitVarInsn(ILOAD, 5);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;", false);
        mv.visitTypeInsn(CHECKCAST, "gregtech/api/objects/MaterialStack");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/Materials", "AnyIron", "Lgregtech/api/enums/Materials;");
        Label l20 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l20);
        Label l21 = new Label();
        mv.visitLabel(l21);
        mv.visitLineNumber(436, l21);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("iron");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        mv.visitLabel(l20);
        mv.visitLineNumber(433, l20);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitIincInsn(5, 1);
        mv.visitLabel(l17);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ILOAD, 5);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/api/objects/ItemData",
                "getAllMaterialStacks",
                "()Ljava/util/ArrayList;",
                false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "size", "()I", false);
        mv.visitJumpInsn(IF_ICMPLT, l18);
        Label l22 = new Label();
        mv.visitLabel(l22);
        mv.visitLineNumber(439, l22);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l15);
        mv.visitFrame(F_CHOP, 1, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/OrePrefixes", "crushed", "Lgregtech/api/enums/OrePrefixes;");
        Label l23 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l23);
        Label l24 = new Label();
        mv.visitLabel(l24);
        mv.visitLineNumber(440, l24);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("crushed");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l25 = new Label();
        mv.visitLabel(l25);
        mv.visitLineNumber(441, l25);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l23);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(
                GETSTATIC, "gregtech/api/enums/OrePrefixes", "crushedPurified", "Lgregtech/api/enums/OrePrefixes;");
        Label l26 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l26);
        Label l27 = new Label();
        mv.visitLabel(l27);
        mv.visitLineNumber(442, l27);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("washing");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l28 = new Label();
        mv.visitLabel(l28);
        mv.visitLineNumber(443, l28);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l26);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(
                GETSTATIC, "gregtech/api/enums/OrePrefixes", "crushedCentrifuged", "Lgregtech/api/enums/OrePrefixes;");
        Label l29 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l29);
        Label l30 = new Label();
        mv.visitLabel(l30);
        mv.visitLineNumber(444, l30);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("spinit");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l31 = new Label();
        mv.visitLabel(l31);
        mv.visitLineNumber(445, l31);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l29);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/ItemData", "mMaterial", "Lgregtech/api/objects/MaterialStack;");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/Materials", "Steel", "Lgregtech/api/enums/Materials;");
        Label l32 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l32);
        Label l33 = new Label();
        mv.visitLabel(l33);
        mv.visitLineNumber(446, l33);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/OrePrefixes", "ingot", "Lgregtech/api/enums/OrePrefixes;");
        Label l34 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l34);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitFieldInsn(GETFIELD, "net/minecraft/item/ItemStack", "stackSize", "I");
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getMaxStackSize", "()I", false);
        mv.visitJumpInsn(IF_ICMPNE, l34);
        Label l35 = new Label();
        mv.visitLabel(l35);
        mv.visitLineNumber(447, l35);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("steel");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l36 = new Label();
        mv.visitLabel(l36);
        mv.visitLineNumber(448, l36);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l34);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/OrePrefixes", "nugget", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitJumpInsn(IF_ACMPNE, l6);
        mv.visitLdcInsn("Thaumcraft");
        mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/Loader", "isModLoaded", "(Ljava/lang/String;)Z", false);
        mv.visitJumpInsn(IFEQ, l6);
        Label l37 = new Label();
        mv.visitLabel(l37);
        mv.visitLineNumber(449, l37);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "net/minecraft/entity/player/EntityPlayer",
                "getDisplayName",
                "()Ljava/lang/String;",
                false);
        mv.visitLdcInsn("GT_IRON_TO_STEEL");
        mv.visitMethodInsn(
                INVOKESTATIC,
                "thaumcraft/api/ThaumcraftApiHelper",
                "isResearchComplete",
                "(Ljava/lang/String;Ljava/lang/String;)Z",
                false);
        mv.visitJumpInsn(IFEQ, l6);
        Label l38 = new Label();
        mv.visitLabel(l38);
        mv.visitLineNumber(450, l38);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("steel");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l39 = new Label();
        mv.visitLabel(l39);
        mv.visitLineNumber(453, l39);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l32);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/OrePrefixes", "circuit", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitJumpInsn(IF_ACMPNE, l6);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/ItemData", "mMaterial", "Lgregtech/api/objects/MaterialStack;");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/Materials", "Advanced", "Lgregtech/api/enums/Materials;");
        mv.visitJumpInsn(IF_ACMPNE, l6);
        Label l40 = new Label();
        mv.visitLabel(l40);
        mv.visitLineNumber(454, l40);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("stepforward");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        mv.visitLabel(l6);
        mv.visitLineNumber(457, l6);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false);
        Label l41 = new Label();
        mv.visitJumpInsn(IFEQ, l41);
        Label l42 = new Label();
        mv.visitLabel(l42);
        mv.visitLineNumber(458, l42);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32500");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l43 = new Label();
        mv.visitJumpInsn(IFEQ, l43);
        Label l44 = new Label();
        mv.visitLabel(l44);
        mv.visitLineNumber(459, l44);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestlead");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l45 = new Label();
        mv.visitLabel(l45);
        mv.visitLineNumber(460, l45);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l43);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32501");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l46 = new Label();
        mv.visitJumpInsn(IFEQ, l46);
        Label l47 = new Label();
        mv.visitLabel(l47);
        mv.visitLineNumber(461, l47);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestsilver");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l48 = new Label();
        mv.visitLabel(l48);
        mv.visitLineNumber(462, l48);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l46);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32503");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l49 = new Label();
        mv.visitJumpInsn(IFEQ, l49);
        Label l50 = new Label();
        mv.visitLabel(l50);
        mv.visitLineNumber(463, l50);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestiron");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l51 = new Label();
        mv.visitLabel(l51);
        mv.visitLineNumber(464, l51);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l49);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32504");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l52 = new Label();
        mv.visitJumpInsn(IFEQ, l52);
        Label l53 = new Label();
        mv.visitLabel(l53);
        mv.visitLineNumber(465, l53);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestgold");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l54 = new Label();
        mv.visitLabel(l54);
        mv.visitLineNumber(466, l54);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l52);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32530");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l55 = new Label();
        mv.visitJumpInsn(IFEQ, l55);
        Label l56 = new Label();
        mv.visitLabel(l56);
        mv.visitLineNumber(467, l56);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestcopper");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l57 = new Label();
        mv.visitLabel(l57);
        mv.visitLineNumber(468, l57);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l55);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32540");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l58 = new Label();
        mv.visitJumpInsn(IFEQ, l58);
        Label l59 = new Label();
        mv.visitLabel(l59);
        mv.visitLineNumber(469, l59);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havesttin");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l60 = new Label();
        mv.visitLabel(l60);
        mv.visitLineNumber(470, l60);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l58);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32510");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l61 = new Label();
        mv.visitJumpInsn(IFEQ, l61);
        Label l62 = new Label();
        mv.visitLabel(l62);
        mv.visitLineNumber(471, l62);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestoil");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l63 = new Label();
        mv.visitLabel(l63);
        mv.visitLineNumber(472, l63);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l61);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32511");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l64 = new Label();
        mv.visitJumpInsn(IFEQ, l64);
        Label l65 = new Label();
        mv.visitLabel(l65);
        mv.visitLineNumber(473, l65);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestemeralds");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l66 = new Label();
        mv.visitLabel(l66);
        mv.visitLineNumber(474, l66);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l64);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32706");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l67 = new Label();
        mv.visitJumpInsn(IFEQ, l67);
        Label l68 = new Label();
        mv.visitLabel(l68);
        mv.visitLineNumber(475, l68);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("energyflow");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l69 = new Label();
        mv.visitLabel(l69);
        mv.visitLineNumber(476, l69);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l67);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32702");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l70 = new Label();
        mv.visitJumpInsn(IFEQ, l70);
        Label l71 = new Label();
        mv.visitLabel(l71);
        mv.visitLineNumber(477, l71);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("bettercircuits");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l72 = new Label();
        mv.visitLabel(l72);
        mv.visitLineNumber(478, l72);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l70);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32707");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l73 = new Label();
        mv.visitJumpInsn(IFEQ, l73);
        Label l74 = new Label();
        mv.visitLabel(l74);
        mv.visitLineNumber(479, l74);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("datasaving");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l75 = new Label();
        mv.visitLabel(l75);
        mv.visitLineNumber(480, l75);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l73);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32597");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l76 = new Label();
        mv.visitJumpInsn(IFEQ, l76);
        Label l77 = new Label();
        mv.visitLabel(l77);
        mv.visitLineNumber(481, l77);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("orbs");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l78 = new Label();
        mv.visitLabel(l78);
        mv.visitLineNumber(482, l78);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l76);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32599");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l79 = new Label();
        mv.visitJumpInsn(IFEQ, l79);
        Label l80 = new Label();
        mv.visitLabel(l80);
        mv.visitLineNumber(483, l80);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("thatspower");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l81 = new Label();
        mv.visitLabel(l81);
        mv.visitLineNumber(484, l81);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l79);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32598");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l82 = new Label();
        mv.visitJumpInsn(IFEQ, l82);
        Label l83 = new Label();
        mv.visitLabel(l83);
        mv.visitLineNumber(485, l83);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("luck");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l84 = new Label();
        mv.visitLabel(l84);
        mv.visitLineNumber(486, l84);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l82);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32749");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l85 = new Label();
        mv.visitJumpInsn(IFEQ, l85);
        Label l86 = new Label();
        mv.visitLabel(l86);
        mv.visitLineNumber(487, l86);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("closeit");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l87 = new Label();
        mv.visitLabel(l87);
        mv.visitLineNumber(488, l87);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l85);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32730");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l88 = new Label();
        mv.visitJumpInsn(IFEQ, l88);
        Label l89 = new Label();
        mv.visitLabel(l89);
        mv.visitLineNumber(489, l89);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("manipulation");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l90 = new Label();
        mv.visitLabel(l90);
        mv.visitLineNumber(490, l90);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l88);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32729");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l91 = new Label();
        mv.visitJumpInsn(IFEQ, l91);
        Label l92 = new Label();
        mv.visitLabel(l92);
        mv.visitLineNumber(491, l92);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("filterregulate");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l93 = new Label();
        mv.visitLabel(l93);
        mv.visitLineNumber(492, l93);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l91);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32605");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l94 = new Label();
        mv.visitJumpInsn(IFEQ, l94);
        Label l95 = new Label();
        mv.visitLabel(l95);
        mv.visitLineNumber(493, l95);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("whatnow");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l96 = new Label();
        mv.visitLabel(l96);
        mv.visitLineNumber(494, l96);
        mv.visitJumpInsn(GOTO, l41);
        mv.visitLabel(l94);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.Thoriumcell");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        mv.visitJumpInsn(IFEQ, l41);
        Label l97 = new Label();
        mv.visitLabel(l97);
        mv.visitLineNumber(495, l97);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("newfuel");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        mv.visitLabel(l41);
        mv.visitLineNumber(498, l41);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitInsn(RETURN);
        Label l98 = new Label();
        mv.visitLabel(l98);
        mv.visitLocalVariable("this", "Lgregtech/loaders/misc/GT_Achievements;", null, l0, l98, 0);
        mv.visitLocalVariable(
                "event", "Lnet/minecraftforge/event/entity/player/EntityItemPickupEvent;", null, l0, l98, 1);
        mv.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l1, l98, 2);
        mv.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l2, l98, 3);
        mv.visitLocalVariable("data", "Lgregtech/api/objects/ItemData;", null, l5, l98, 4);
        mv.visitLocalVariable("i", "I", null, l16, l22, 5);
        mv.visitMaxs(4, 6);
        mv.visitEnd();

        didInject = true;
        FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Method injection complete.");
        return didInject;
    }

    public boolean patchOnItemPickup09(ClassWriter cw) {
        MethodVisitor mv;
        boolean didInject = false;
        FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Injecting " + "onItemPickup" + ".");

        /**
         * Inject new, safer code
         */
        AnnotationVisitor av0;
        mv = cw.visitMethod(
                ACC_PUBLIC,
                "onItemPickup",
                "(Lnet/minecraftforge/event/entity/player/EntityItemPickupEvent;)V",
                null,
                null);
        av0 = mv.visitAnnotation("Lcpw/mods/fml/common/eventhandler/SubscribeEvent;", true);
        av0.visitEnd();
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(546, l0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(
                GETFIELD,
                "net/minecraftforge/event/entity/player/EntityItemPickupEvent",
                "entityPlayer",
                "Lnet/minecraft/entity/player/EntityPlayer;");
        mv.visitVarInsn(ASTORE, 2);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(547, l1);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(
                GETFIELD,
                "net/minecraftforge/event/entity/player/EntityItemPickupEvent",
                "item",
                "Lnet/minecraft/entity/item/EntityItem;");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "net/minecraft/entity/item/EntityItem",
                "getEntityItem",
                "()Lnet/minecraft/item/ItemStack;",
                false);
        mv.visitVarInsn(ASTORE, 3);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(548, l2);
        mv.visitVarInsn(ALOAD, 2);
        Label l3 = new Label();
        mv.visitJumpInsn(IFNULL, l3);
        mv.visitVarInsn(ALOAD, 3);
        Label l4 = new Label();
        mv.visitJumpInsn(IFNONNULL, l4);
        mv.visitLabel(l3);
        mv.visitLineNumber(549, l3);
        mv.visitFrame(
                F_APPEND,
                2,
                new Object[] {"net/minecraft/entity/player/EntityPlayer", "net/minecraft/item/ItemStack"},
                0,
                null);
        mv.visitInsn(RETURN);
        mv.visitLabel(l4);
        mv.visitLineNumber(551, l4);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "gregtech/api/util/GT_OreDictUnificator",
                "getItemData",
                "(Lnet/minecraft/item/ItemStack;)Lgregtech/api/objects/ItemData;",
                false);
        mv.visitVarInsn(ASTORE, 4);
        Label l5 = new Label();
        mv.visitLabel(l5);
        mv.visitLineNumber(552, l5);
        mv.visitVarInsn(ALOAD, 4);
        Label l6 = new Label();
        mv.visitJumpInsn(IFNULL, l6);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitJumpInsn(IFNULL, l6);
        Label l7 = new Label();
        mv.visitLabel(l7);
        mv.visitLineNumber(553, l7);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/OrePrefixes", "dust", "Lgregtech/api/enums/OrePrefixes;");
        Label l8 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l8);
        Label l9 = new Label();
        mv.visitLabel(l9);
        mv.visitLineNumber(554, l9);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/ItemData", "mMaterial", "Lgregtech/api/objects/MaterialStack;");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/Materials", "Lutetium", "Lgregtech/api/enums/Materials;");
        Label l10 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l10);
        Label l11 = new Label();
        mv.visitLabel(l11);
        mv.visitLineNumber(555, l11);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("newmetal");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        mv.visitLabel(l10);
        mv.visitLineNumber(557, l10);
        mv.visitFrame(F_APPEND, 1, new Object[] {"gregtech/api/objects/ItemData"}, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/ItemData", "mMaterial", "Lgregtech/api/objects/MaterialStack;");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/Materials", "Gunpowder", "Lgregtech/api/enums/Materials;");
        mv.visitJumpInsn(IF_ACMPEQ, l6);
        Label l12 = new Label();
        mv.visitLabel(l12);
        mv.visitLineNumber(558, l12);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("cleandust");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l13 = new Label();
        mv.visitLabel(l13);
        mv.visitLineNumber(560, l13);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l8);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/enums/OrePrefixes", "name", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("ore");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false);
        Label l14 = new Label();
        mv.visitJumpInsn(IFEQ, l14);
        Label l15 = new Label();
        mv.visitLabel(l15);
        mv.visitLineNumber(561, l15);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/api/objects/ItemData",
                "getAllMaterialStacks",
                "()Ljava/util/ArrayList;",
                false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "size", "()I", false);
        mv.visitVarInsn(ISTORE, 5);
        Label l16 = new Label();
        mv.visitLabel(l16);
        mv.visitLineNumber(562, l16);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, 6);
        Label l17 = new Label();
        mv.visitLabel(l17);
        Label l18 = new Label();
        mv.visitJumpInsn(GOTO, l18);
        Label l19 = new Label();
        mv.visitLabel(l19);
        mv.visitLineNumber(563, l19);
        mv.visitFrame(F_APPEND, 2, new Object[] {INTEGER, INTEGER}, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/api/objects/ItemData",
                "getAllMaterialStacks",
                "()Ljava/util/ArrayList;",
                false);
        mv.visitVarInsn(ILOAD, 6);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;", false);
        mv.visitTypeInsn(CHECKCAST, "gregtech/api/objects/MaterialStack");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETFIELD, "gregtech/api/enums/Materials", "mName", "Ljava/lang/String;");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l20 = new Label();
        mv.visitLabel(l20);
        mv.visitLineNumber(564, l20);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/api/objects/ItemData",
                "getAllMaterialStacks",
                "()Ljava/util/ArrayList;",
                false);
        mv.visitVarInsn(ILOAD, 6);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;", false);
        mv.visitTypeInsn(CHECKCAST, "gregtech/api/objects/MaterialStack");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/Materials", "Iron", "Lgregtech/api/enums/Materials;");
        Label l21 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l21);
        Label l22 = new Label();
        mv.visitLabel(l22);
        mv.visitLineNumber(565, l22);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("iron");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        mv.visitLabel(l21);
        mv.visitLineNumber(567, l21);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/api/objects/ItemData",
                "getAllMaterialStacks",
                "()Ljava/util/ArrayList;",
                false);
        mv.visitVarInsn(ILOAD, 6);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;", false);
        mv.visitTypeInsn(CHECKCAST, "gregtech/api/objects/MaterialStack");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/Materials", "Copper", "Lgregtech/api/enums/Materials;");
        Label l23 = new Label();
        mv.visitJumpInsn(IF_ACMPEQ, l23);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/api/objects/ItemData",
                "getAllMaterialStacks",
                "()Ljava/util/ArrayList;",
                false);
        mv.visitVarInsn(ILOAD, 6);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;", false);
        mv.visitTypeInsn(CHECKCAST, "gregtech/api/objects/MaterialStack");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/Materials", "Tin", "Lgregtech/api/enums/Materials;");
        Label l24 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l24);
        mv.visitLabel(l23);
        mv.visitLineNumber(568, l23);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(
                GETFIELD,
                "net/minecraftforge/event/entity/player/EntityItemPickupEvent",
                "entityPlayer",
                "Lnet/minecraft/entity/player/EntityPlayer;");
        mv.visitLdcInsn("mineOre");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        mv.visitLabel(l24);
        mv.visitLineNumber(562, l24);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitIincInsn(6, 1);
        mv.visitLabel(l18);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ILOAD, 6);
        mv.visitVarInsn(ILOAD, 5);
        mv.visitJumpInsn(IF_ICMPLT, l19);
        Label l25 = new Label();
        mv.visitLabel(l25);
        mv.visitLineNumber(572, l25);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l14);
        mv.visitFrame(F_CHOP, 2, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/OrePrefixes", "crushed", "Lgregtech/api/enums/OrePrefixes;");
        Label l26 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l26);
        Label l27 = new Label();
        mv.visitLabel(l27);
        mv.visitLineNumber(573, l27);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("crushed");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l28 = new Label();
        mv.visitLabel(l28);
        mv.visitLineNumber(574, l28);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l26);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(
                GETSTATIC, "gregtech/api/enums/OrePrefixes", "crushedPurified", "Lgregtech/api/enums/OrePrefixes;");
        Label l29 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l29);
        Label l30 = new Label();
        mv.visitLabel(l30);
        mv.visitLineNumber(575, l30);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("washing");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l31 = new Label();
        mv.visitLabel(l31);
        mv.visitLineNumber(576, l31);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l29);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(
                GETSTATIC, "gregtech/api/enums/OrePrefixes", "crushedCentrifuged", "Lgregtech/api/enums/OrePrefixes;");
        Label l32 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l32);
        Label l33 = new Label();
        mv.visitLabel(l33);
        mv.visitLineNumber(577, l33);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("spinit");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l34 = new Label();
        mv.visitLabel(l34);
        mv.visitLineNumber(578, l34);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l32);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/ItemData", "mMaterial", "Lgregtech/api/objects/MaterialStack;");
        mv.visitFieldInsn(
                GETFIELD, "gregtech/api/objects/MaterialStack", "mMaterial", "Lgregtech/api/enums/Materials;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/Materials", "Steel", "Lgregtech/api/enums/Materials;");
        mv.visitJumpInsn(IF_ACMPNE, l6);
        Label l35 = new Label();
        mv.visitLabel(l35);
        mv.visitLineNumber(579, l35);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/OrePrefixes", "ingot", "Lgregtech/api/enums/OrePrefixes;");
        Label l36 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l36);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitFieldInsn(GETFIELD, "net/minecraft/item/ItemStack", "stackSize", "I");
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getMaxStackSize", "()I", false);
        mv.visitJumpInsn(IF_ICMPNE, l36);
        Label l37 = new Label();
        mv.visitLabel(l37);
        mv.visitLineNumber(580, l37);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("steel");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l38 = new Label();
        mv.visitLabel(l38);
        mv.visitLineNumber(581, l38);
        mv.visitJumpInsn(GOTO, l6);
        mv.visitLabel(l36);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitFieldInsn(GETFIELD, "gregtech/api/objects/ItemData", "mPrefix", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitFieldInsn(GETSTATIC, "gregtech/api/enums/OrePrefixes", "nugget", "Lgregtech/api/enums/OrePrefixes;");
        mv.visitJumpInsn(IF_ACMPNE, l6);
        mv.visitLdcInsn("Thaumcraft");
        mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/Loader", "isModLoaded", "(Ljava/lang/String;)Z", false);
        mv.visitJumpInsn(IFEQ, l6);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "net/minecraft/entity/player/EntityPlayer",
                "getDisplayName",
                "()Ljava/lang/String;",
                false);
        mv.visitLdcInsn("GT_IRON_TO_STEEL");
        mv.visitMethodInsn(
                INVOKESTATIC,
                "thaumcraft/api/ThaumcraftApiHelper",
                "isResearchComplete",
                "(Ljava/lang/String;Ljava/lang/String;)Z",
                false);
        mv.visitJumpInsn(IFEQ, l6);
        Label l39 = new Label();
        mv.visitLabel(l39);
        mv.visitLineNumber(582, l39);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("steel");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        mv.visitLabel(l6);
        mv.visitLineNumber(589, l6);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false);
        Label l40 = new Label();
        mv.visitJumpInsn(IFEQ, l40);
        Label l41 = new Label();
        mv.visitLabel(l41);
        mv.visitLineNumber(590, l41);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32500");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l42 = new Label();
        mv.visitJumpInsn(IFEQ, l42);
        Label l43 = new Label();
        mv.visitLabel(l43);
        mv.visitLineNumber(591, l43);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestlead");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l44 = new Label();
        mv.visitLabel(l44);
        mv.visitLineNumber(592, l44);
        Label l45 = new Label();
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l42);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32501");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l46 = new Label();
        mv.visitJumpInsn(IFEQ, l46);
        Label l47 = new Label();
        mv.visitLabel(l47);
        mv.visitLineNumber(593, l47);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestsilver");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l48 = new Label();
        mv.visitLabel(l48);
        mv.visitLineNumber(594, l48);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l46);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32503");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l49 = new Label();
        mv.visitJumpInsn(IFEQ, l49);
        Label l50 = new Label();
        mv.visitLabel(l50);
        mv.visitLineNumber(595, l50);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestiron");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l51 = new Label();
        mv.visitLabel(l51);
        mv.visitLineNumber(596, l51);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l49);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32504");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l52 = new Label();
        mv.visitJumpInsn(IFEQ, l52);
        Label l53 = new Label();
        mv.visitLabel(l53);
        mv.visitLineNumber(597, l53);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestgold");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l54 = new Label();
        mv.visitLabel(l54);
        mv.visitLineNumber(598, l54);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l52);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32530");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l55 = new Label();
        mv.visitJumpInsn(IFEQ, l55);
        Label l56 = new Label();
        mv.visitLabel(l56);
        mv.visitLineNumber(599, l56);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestcopper");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l57 = new Label();
        mv.visitLabel(l57);
        mv.visitLineNumber(600, l57);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l55);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32540");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l58 = new Label();
        mv.visitJumpInsn(IFEQ, l58);
        Label l59 = new Label();
        mv.visitLabel(l59);
        mv.visitLineNumber(601, l59);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havesttin");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l60 = new Label();
        mv.visitLabel(l60);
        mv.visitLineNumber(602, l60);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l58);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32510");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l61 = new Label();
        mv.visitJumpInsn(IFEQ, l61);
        Label l62 = new Label();
        mv.visitLabel(l62);
        mv.visitLineNumber(603, l62);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestoil");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l63 = new Label();
        mv.visitLabel(l63);
        mv.visitLineNumber(604, l63);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l61);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.02.32511");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l64 = new Label();
        mv.visitJumpInsn(IFEQ, l64);
        Label l65 = new Label();
        mv.visitLabel(l65);
        mv.visitLineNumber(605, l65);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("havestemeralds");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l66 = new Label();
        mv.visitLabel(l66);
        mv.visitLineNumber(606, l66);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l64);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.03.32082");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l67 = new Label();
        mv.visitJumpInsn(IFEQ, l67);
        Label l68 = new Label();
        mv.visitLabel(l68);
        mv.visitLineNumber(607, l68);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("energyflow");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l69 = new Label();
        mv.visitLabel(l69);
        mv.visitLineNumber(608, l69);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l67);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32702");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l70 = new Label();
        mv.visitJumpInsn(IFEQ, l70);
        Label l71 = new Label();
        mv.visitLabel(l71);
        mv.visitLineNumber(609, l71);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("bettercircuits");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l72 = new Label();
        mv.visitLabel(l72);
        mv.visitLineNumber(610, l72);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l70);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32707");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l73 = new Label();
        mv.visitJumpInsn(IFEQ, l73);
        Label l74 = new Label();
        mv.visitLabel(l74);
        mv.visitLineNumber(611, l74);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("datasaving");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l75 = new Label();
        mv.visitLabel(l75);
        mv.visitLineNumber(612, l75);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l73);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32597");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l76 = new Label();
        mv.visitJumpInsn(IFEQ, l76);
        Label l77 = new Label();
        mv.visitLabel(l77);
        mv.visitLineNumber(613, l77);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("orbs");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l78 = new Label();
        mv.visitLabel(l78);
        mv.visitLineNumber(614, l78);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l76);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32599");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l79 = new Label();
        mv.visitJumpInsn(IFEQ, l79);
        Label l80 = new Label();
        mv.visitLabel(l80);
        mv.visitLineNumber(615, l80);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("thatspower");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l81 = new Label();
        mv.visitLabel(l81);
        mv.visitLineNumber(616, l81);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l79);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32598");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l82 = new Label();
        mv.visitJumpInsn(IFEQ, l82);
        Label l83 = new Label();
        mv.visitLabel(l83);
        mv.visitLineNumber(617, l83);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("luck");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l84 = new Label();
        mv.visitLabel(l84);
        mv.visitLineNumber(618, l84);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l82);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32749");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l85 = new Label();
        mv.visitJumpInsn(IFEQ, l85);
        Label l86 = new Label();
        mv.visitLabel(l86);
        mv.visitLineNumber(619, l86);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("closeit");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l87 = new Label();
        mv.visitLabel(l87);
        mv.visitLineNumber(620, l87);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l85);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32730");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l88 = new Label();
        mv.visitJumpInsn(IFEQ, l88);
        Label l89 = new Label();
        mv.visitLabel(l89);
        mv.visitLineNumber(621, l89);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("manipulation");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l90 = new Label();
        mv.visitLabel(l90);
        mv.visitLineNumber(622, l90);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l88);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32729");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l91 = new Label();
        mv.visitJumpInsn(IFEQ, l91);
        Label l92 = new Label();
        mv.visitLabel(l92);
        mv.visitLineNumber(623, l92);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("filterregulate");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l93 = new Label();
        mv.visitLabel(l93);
        mv.visitLineNumber(624, l93);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l91);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32605");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l94 = new Label();
        mv.visitJumpInsn(IFEQ, l94);
        Label l95 = new Label();
        mv.visitLabel(l95);
        mv.visitLineNumber(625, l95);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("whatnow");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l96 = new Label();
        mv.visitLabel(l96);
        mv.visitLineNumber(626, l96);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l94);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32736");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l97 = new Label();
        mv.visitJumpInsn(IFEQ, l97);
        Label l98 = new Label();
        mv.visitLabel(l98);
        mv.visitLineNumber(627, l98);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("zpmage");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l99 = new Label();
        mv.visitLabel(l99);
        mv.visitLineNumber(628, l99);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l97);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32737");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l100 = new Label();
        mv.visitJumpInsn(IFEQ, l100);
        Label l101 = new Label();
        mv.visitLabel(l101);
        mv.visitLineNumber(629, l101);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("uvage");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l102 = new Label();
        mv.visitLabel(l102);
        mv.visitLineNumber(630, l102);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l100);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.03.32030");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l103 = new Label();
        mv.visitJumpInsn(IFEQ, l103);
        Label l104 = new Label();
        mv.visitLabel(l104);
        mv.visitLineNumber(631, l104);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("gtmonosilicon");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l105 = new Label();
        mv.visitLabel(l105);
        mv.visitLineNumber(632, l105);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l103);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.03.32036");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l106 = new Label();
        mv.visitJumpInsn(IFEQ, l106);
        Label l107 = new Label();
        mv.visitLabel(l107);
        mv.visitLineNumber(633, l107);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("gtlogicwafer");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l108 = new Label();
        mv.visitLabel(l108);
        mv.visitLineNumber(634, l108);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l106);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32701");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l109 = new Label();
        mv.visitJumpInsn(IFEQ, l109);
        Label l110 = new Label();
        mv.visitLabel(l110);
        mv.visitLineNumber(635, l110);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("gtlogiccircuit");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l111 = new Label();
        mv.visitLabel(l111);
        mv.visitLineNumber(636, l111);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l109);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.03.32085");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l112 = new Label();
        mv.visitJumpInsn(IFEQ, l112);
        Label l113 = new Label();
        mv.visitLabel(l113);
        mv.visitLineNumber(637, l113);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("gtquantumprocessor");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l114 = new Label();
        mv.visitLabel(l114);
        mv.visitLineNumber(638, l114);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l112);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.03.32089");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l115 = new Label();
        mv.visitJumpInsn(IFEQ, l115);
        Label l116 = new Label();
        mv.visitLabel(l116);
        mv.visitLineNumber(639, l116);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("gtcrystalprocessor");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l117 = new Label();
        mv.visitLabel(l117);
        mv.visitLineNumber(640, l117);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l115);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.03.32092");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l118 = new Label();
        mv.visitJumpInsn(IFEQ, l118);
        Label l119 = new Label();
        mv.visitLabel(l119);
        mv.visitLineNumber(641, l119);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("gtwetware");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l120 = new Label();
        mv.visitLabel(l120);
        mv.visitLineNumber(642, l120);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l118);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.03.32095");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l121 = new Label();
        mv.visitJumpInsn(IFEQ, l121);
        Label l122 = new Label();
        mv.visitLabel(l122);
        mv.visitLineNumber(643, l122);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("gtwetmain");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l123 = new Label();
        mv.visitLabel(l123);
        mv.visitLineNumber(644, l123);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l121);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32736");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l124 = new Label();
        mv.visitJumpInsn(IFEQ, l124);
        Label l125 = new Label();
        mv.visitLabel(l125);
        mv.visitLineNumber(645, l125);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("zpmage");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l126 = new Label();
        mv.visitLabel(l126);
        mv.visitLineNumber(646, l126);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l124);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.metaitem.01.32737");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        mv.visitJumpInsn(IFEQ, l45);
        Label l127 = new Label();
        mv.visitLabel(l127);
        mv.visitLineNumber(647, l127);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("uvage");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l128 = new Label();
        mv.visitLabel(l128);
        mv.visitLineNumber(649, l128);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l40);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("gt.Thoriumcell");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        Label l129 = new Label();
        mv.visitJumpInsn(IFEQ, l129);
        Label l130 = new Label();
        mv.visitLabel(l130);
        mv.visitLineNumber(650, l130);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("newfuel");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l131 = new Label();
        mv.visitLabel(l131);
        mv.visitLineNumber(651, l131);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l129);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;", false);
        mv.visitFieldInsn(GETSTATIC, "ic2/core/Ic2Items", "quantumBodyarmor", "Lnet/minecraft/item/ItemStack;");
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;", false);
        Label l132 = new Label();
        mv.visitJumpInsn(IF_ACMPEQ, l132);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;", false);
        mv.visitFieldInsn(GETSTATIC, "ic2/core/Ic2Items", "quantumBoots", "Lnet/minecraft/item/ItemStack;");
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;", false);
        mv.visitJumpInsn(IF_ACMPEQ, l132);
        Label l133 = new Label();
        mv.visitLabel(l133);
        mv.visitLineNumber(652, l133);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;", false);
        mv.visitFieldInsn(GETSTATIC, "ic2/core/Ic2Items", "quantumHelmet", "Lnet/minecraft/item/ItemStack;");
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;", false);
        mv.visitJumpInsn(IF_ACMPEQ, l132);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;", false);
        mv.visitFieldInsn(GETSTATIC, "ic2/core/Ic2Items", "quantumLeggings", "Lnet/minecraft/item/ItemStack;");
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;", false);
        Label l134 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l134);
        mv.visitLabel(l132);
        mv.visitLineNumber(653, l132);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("buildQArmor");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        Label l135 = new Label();
        mv.visitLabel(l135);
        mv.visitLineNumber(654, l135);
        mv.visitJumpInsn(GOTO, l45);
        mv.visitLabel(l134);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
        mv.visitLdcInsn("ic2.itemPartCircuitAdv");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
        mv.visitJumpInsn(IFEQ, l45);
        Label l136 = new Label();
        mv.visitLabel(l136);
        mv.visitLineNumber(655, l136);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitLdcInsn("stepforward");
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "gregtech/loaders/misc/GT_Achievements",
                "issueAchievement",
                "(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)V",
                false);
        mv.visitLabel(l45);
        mv.visitLineNumber(657, l45);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitInsn(RETURN);
        Label l137 = new Label();
        mv.visitLabel(l137);
        mv.visitLocalVariable("this", "Lgregtech/loaders/misc/GT_Achievements;", null, l0, l137, 0);
        mv.visitLocalVariable(
                "event", "Lnet/minecraftforge/event/entity/player/EntityItemPickupEvent;", null, l0, l137, 1);
        mv.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l1, l137, 2);
        mv.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l2, l137, 3);
        mv.visitLocalVariable("data", "Lgregtech/api/objects/ItemData;", null, l5, l137, 4);
        mv.visitLocalVariable("data_getAllMaterialStacks_sS", "I", null, l16, l25, 5);
        mv.visitLocalVariable("i", "I", null, l17, l25, 6);
        mv.visitMaxs(4, 7);
        mv.visitEnd();

        didInject = true;
        FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Method injection complete.");
        return didInject;
    }

    public class MethodAdaptor extends ClassVisitor {

        public MethodAdaptor(ClassVisitor cv) {
            super(ASM5, cv);
            this.cv = cv;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor methodVisitor;
            if (name.equals("registerAssAchievement") || name.equals("onItemPickup")) {
                FMLRelaunchLog.log(
                        "[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Found method " + name + ", removing.");
                methodVisitor = null;
                if (name.equals("registerAssAchievement")) {
                    mDidRemoveAssLineRecipeAdder = true;
                }
            } else {
                methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
            }
            return methodVisitor;
        }
    }
}
