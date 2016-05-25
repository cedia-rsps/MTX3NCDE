package cedia.ncde.io

import cedia.ncde.model.CombatDefinition
import cedia.ncde.model.DefinitionFileType
import cedia.ncde.model.NPC
import java.io.*
import java.util.*

/**
 * Created on 2016-05-22.
 */
object CombatDefinitionsIO {

    fun save(list: Collection<NPC>, type: DefinitionFileType, file: File) {
        when (type) {
            DefinitionFileType.UNPACKED -> {
                BufferedWriter(FileWriter(file)).use {
                    for (npc in list) {
                        val def = npc.combatDefinition
                        //could have used reflection, but nah waste since we are only using it once here
                        //order would be messed up too
                        it.write("//${npc.name}, Id: ${npc.id}")
                        it.newLine()
                        it.write("${npc.id} - ")
                        it.write("${def.hitPoints} " +
                                "${def.attackAnimation} " +
                                "${def.defenceAnimation} " +
                                "${def.deathAnimation} " +
                                "${def.respawnDelay} " +
                                "${def.attackGraphics} " +
                                "${def.attackProjectile} " +
                                "${def.xp} " +
                                "${def.followClose} " +
                                "${def.poisonImmune} " +
                                "${def.poisonous} " +
                                "${def.aggressive} " +
                                "${def.aggressiveRatio}")
                        it.newLine()
                    }
                }
            }
            DefinitionFileType.PACKED -> {
                DataOutputStream(FileOutputStream(file)).use { out ->
                    for (npc in list) {
                        val def = npc.combatDefinition
                        val npcId = npc.id
                        val hitpoints = def.hitPoints
                        val attackAnim = def.attackAnimation
                        val defenceAnim = def.defenceAnimation
                        val deathAnim = def.deathAnimation
                        val respawnDelay = def.respawnDelay
                        val attackGfx = def.attackGraphics
                        val attackProjectile = def.attackProjectile
                        val xp = def.xp
                        val follow = def.followClose
                        val poisonImmune = def.poisonImmune
                        val poisonous = def.poisonous
                        val agressivenessType = def.aggressive
                        val agroRatio = def.aggressiveRatio
                        out.writeShort(npcId)
                        out.writeInt(hitpoints)
                        out.writeShort(attackAnim)
                        out.writeShort(defenceAnim)
                        out.writeShort(deathAnim)
                        out.writeInt(respawnDelay)
                        out.writeShort(attackGfx)
                        out.writeShort(attackProjectile)
                        out.writeDouble(xp)
                        out.writeByte(if (follow) 1 else 0)
                        out.writeByte(if (poisonImmune) 1 else 0)
                        out.writeByte(if (poisonous) 1 else 0)
                        out.writeByte(if (agressivenessType) 1 else 0)
                        out.writeByte(agroRatio)
                    }

                }
            }
        }
    }

    fun load(type: DefinitionFileType, file: File): ArrayList<NPC> {
        val definitions: ArrayList<NPC> = ArrayList()
        when (type) {
            DefinitionFileType.UNPACKED -> {
                var count = 0
                BufferedReader(FileReader(file)).use {
                    while (true) {
                        val line = it.readLine()
                        count++
                        if (line == null)
                            break
                        if (line.startsWith("//"))
                            continue
                        val splitedLine = line.split(" - ".toRegex(), 2).toTypedArray()
                        if (splitedLine.size != 2) {
                            throw RuntimeException("Invalid NPC Combat Definitions line: $count, $line")
                        }
                        val npcId = Integer.parseInt(splitedLine[0])
                        val splitedLine2 = splitedLine[1].split(" ".toRegex(), 13).toTypedArray()
                        if (splitedLine2.size != 13) {
                            throw RuntimeException("Invalid NPC Combat Definitions line: $count, $line")
                        }
                        val hitpoints = Integer.parseInt(splitedLine2[0])
                        val attackAnim = Integer.parseInt(splitedLine2[1])
                        val defenceAnim = Integer.parseInt(splitedLine2[2])
                        val deathAnim = Integer.parseInt(splitedLine2[3])
                        val respawnDelay = Integer.parseInt(splitedLine2[4])
                        val attackGfx = Integer.parseInt(splitedLine2[5])
                        val attackProjectile = Integer.parseInt(splitedLine2[6])
                        val xp = java.lang.Double.parseDouble(splitedLine2[7])
                        val follow = splitedLine2[8].toBoolean()
                        val poisonImmune = splitedLine2[9].toBoolean()
                        val poisonous = splitedLine2[10].toBoolean()
                        val aggressivenessType = splitedLine2[11].toBoolean()
                        val agroRatio = Integer.parseInt(splitedLine2[12])
                        val definition = CombatDefinition(hitPoints = hitpoints, attackAnimation = attackAnim,
                                defenceAnimation = defenceAnim, deathAnimation = deathAnim,
                                respawnDelay = respawnDelay, attackGraphics = attackGfx,
                                attackProjectile = attackProjectile, xp = xp, followClose = follow,
                                poisonImmune = poisonImmune, poisonous = poisonous, aggressiveRatio = agroRatio,
                                aggressive = aggressivenessType)
                        val npc = NPC(id = npcId, combatDefinition = definition)
                        definitions.add(npc)
                    }
                }
            }
            DefinitionFileType.PACKED -> {
                DataInputStream(FileInputStream(file)).use { buffer ->
                    while (buffer.available() != 0) {
                        val npcId = buffer.readUnsignedShort()
                        val hitpoints = buffer.readInt()
                        var attackAnim = buffer.readUnsignedShort()
                        if (attackAnim == 65535)
                            attackAnim = -1
                        var defenceAnim = buffer.readUnsignedShort()
                        if (defenceAnim == 65535) {
                            defenceAnim = -1
                        }
                        var deathAnim = buffer.readUnsignedShort()
                        if (deathAnim == 65535)
                            deathAnim = -1
                        val respawnDelay = buffer.readInt()
                        var attackGfx = buffer.readUnsignedShort()
                        if (attackGfx == 65535)
                            attackGfx = -1
                        var attackProjectile = buffer.readUnsignedShort()
                        if (attackProjectile == 65535)
                            attackProjectile = -1
                        val xp = buffer.readDouble()
                        val follow = buffer.readUnsignedByte() === 1
                        val poisonImmune = buffer.readUnsignedByte() === 1
                        val poisonous = buffer.readUnsignedByte() === 1
                        val agressivenessType = buffer.readUnsignedByte() === 1
                        val agroRatio = buffer.readUnsignedByte()
                        val definition = CombatDefinition(hitpoints, attackAnim, defenceAnim, deathAnim, respawnDelay, attackGfx, attackProjectile, xp, follow, poisonImmune, poisonous, agressivenessType, agroRatio)
                        val npc = NPC(id = npcId, combatDefinition = definition)
                        definitions.add(npc)
                    }
                }
            }
        }
        return definitions
    }

}