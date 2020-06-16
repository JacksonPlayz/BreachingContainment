var Opcodes = org.objectweb.asm.Opcodes;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;

function initializeCoreMod() {
    return {
        "SoundSourceTransformer": {
            "target": {
                "type": "CLASS",
                "name": "net.minecraft.client.audio.SoundSource"
            },
            "transformer": patch_sound_source
        }
    }
}

function patch_sound_source(class_node) {
    var api = Java.type('net.minecraftforge.coremod.api.ASMAPI');

    var play_sound_method = get_method(class_node, api.mapMethod("func_216420_a"));
    var delete_sound_method = get_method(class_node, api.mapMethod("func_216436_b"));

    var play_sound_instructions = play_sound_method.instructions;
    for (var i = 0; i < play_sound_instructions.size(); i++) {
        var insn = play_sound_instructions.get(i);
        if (insn.getOpcode() == Opcodes.RETURN) {
            play_sound_instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
            play_sound_instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/audio/SoundSource", api.mapField("field_216441_b"), "I"));
            play_sound_instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
            play_sound_instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "me/jaackson/breach/client/sound/ReverbHandler", "onPlaySound", "(ILnet/minecraft/util/math/Vec3d;)V", false));
            break;
        }
    }

    var delete_sound_instructions = delete_sound_method.instructions;
    var iconst1_count = 0;
    for (var i = 0; i < delete_sound_instructions.size(); i++) {
        var insn = delete_sound_instructions.get(i);
        if(insn.getOpcode() == Opcodes.ICONST_1) {
            iconst1_count++;
        }
        if (iconst1_count == 2) {
            delete_sound_instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
            delete_sound_instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/audio/SoundSource", api.mapField("field_216441_b"), "I"));
            delete_sound_instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "me/jaackson/breach/client/sound/ReverbHandler", "onDeleteSound", "(I)V", false));
            break;
        }
    }

    return class_node;
}

function get_method(class_node, name) {
    for (var index in class_node.methods) {
        var method = class_node.methods[index];
        if (method.name.equals(name)) {
            return method;
        }
    }
    throw "couldn't find method with name '" + name + "' in '" + class_node.name + "'"
}

function log(s) {
    print("[Breaching Containment] " + s);
}