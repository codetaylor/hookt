All parameters are required unless explicitly marked &#x1F539;*Optional*.

## IRuleList

This is the main object of any `.json` rule files.

Rule files are processed in the order in which they are loaded. Files with a higher priority will have their rules matched first.

<big><pre>
{
&nbsp;&nbsp;"priority": int,
&nbsp;&nbsp;"rules": [IRule](#irule)[]
}
</pre></big>

* `priority`: `int`
  * &#x1F539;*Optional* - if omitted, defaults to `0`.
  * Files with a higher priority will have their rules matched first. This value can be positive or negative.

* `rules`: <code>[IRule](#irule)[]</code>
  * This array contains objects which define match and replace behavior.

## IRule

The `IRule` defines match and replace behavior.

Each `IRule` entry is matched in the same order it is defined the file. 

<big><pre>
{
&nbsp;&nbsp;"debug": boolean,
&nbsp;&nbsp;"match": [IRuleMatch](#irulematch),
&nbsp;&nbsp;"replaceStrategy": enum,
&nbsp;&nbsp;"dropStrategy": enum,
&nbsp;&nbsp;"dropCount": [IRandomLuckInt](#irandomluckint),
&nbsp;&nbsp;"drops": [IRuleDrop](#iruledrop)[]
}
</pre></big>

* `debug`: `boolean`
  * &#x1F539;*Optional* - if omitted, defaults to `false`.
  * Prints a large amount of debug data to the log file. Be careful how many rules you enable this flag on, as it will rapidly bloat your log file. Useful for testing.

* `match`: <code>[IRuleMatch](#irulematch)</code>
  * &#x1F539;*Optional* - if omitted, all will be matched.
  * This object defines the conditions used to match this rule. 

* `replaceStrategy`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `REPLACE_ALL`.
  * `REPLACE_ITEMS`: All items defined in the <code>[IRuleMatch](#irulematch)</code> will be removed.
  * `REPLACE_ITEMS_IF_SELECTED`: All items defined in the <code>[IRuleMatch](#irulematch)</code> will be removed if and only if drops are selected from this rule.
  * `REPLACE_ALL`: All drops will be replaced by drops from this rule.
  * `REPLACE_ALL_IF_SELECTED`: All drops will be replaced by drops from this rule if and only if drops are selected from this rule.
  * `ADD`: Any selected drops from this rule will be added to the existing drops. If this rule is matched and no drops are selected, nothing will drop.

* `dropStrategy`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `REPEAT`.
  * `REPEAT`: When picking drops from the weighted picker, any <code>[IRuleDrop](#iruledrop)</code> that is selected may be selected more than once.
  * `UNIQUE`: When picking drops from the weighted picker, any <code>[IRuleDrop](#iruledrop)</code> that is selected will be removed from the picker, ensuring that it will only be selected once. If the picker is depleted before the defined `dropCount` is reached, drop picking will stop.

* `dropCount`: <code>[IRandomLuckInt](#irandomluckint)</code>
  * &#x1F539;*Optional* - if omitted, defaults to `1`.
  * This object defines how many times the weighted picker will be queried for drops.

* `drops`: <code>[IRuleDrop](#iruledrop)[]</code>
  * &#x1F539;*Optional* - if omitted, no drops will be processed.
  * This array defines the potential drops for this rule.

## IRuleMatch

The `IRuleMatch` is responsible for matching a rule using defined conditions. 

When a fishing attempt is made, the first <code>[IRule](#irule)</code> to successfully satisfy the requirements of the `IRuleMatch` is selected.

<big><pre>
{
&nbsp;&nbsp;"drops": [IRuleMatchDrops](#irulematchdrops),
&nbsp;&nbsp;"angler": [IRuleMatchAngler](#irulematchangler),
&nbsp;&nbsp;"biomes": [IRuleMatchBiome](#irulematchbiome),
&nbsp;&nbsp;"dimensions": [IRuleMatchDimension](#irulematchdimension),
&nbsp;&nbsp;"verticalRange": [IRangeInt](#irangeint)
}
</pre></big>

* `drops`: <code>[IRuleMatchDrops](#irulematchdrops)</code>
  * &#x1F539;*Optional* - if omitted, this condition will pass.
  * This object defines items to be matched against the items dropped.

* `angler`: <code>[IRuleMatchAngler](#irulematchangler)</code>
  * &#x1F539;*Optional* - if omitted, any angler will be matched.
  * This object defines conditions specific to the angler.
  
* `biomes`: <code>[IRuleMatchBiome](#irulematchbiome)</code>
  * &#x1F539;*Optional* - if omitted, all biomes will match.
  * This object defines biome conditions.
  
* `dimensions`: <code>[IRuleMatchDimension](#irulematchdimension)</code>
  * &#x1F539;*Optional* - if omitted, all dimensions will match. 
  * This object defines dimension conditions.

* `verticalRange`: <code>[IRangeInt](#irangeint)</code>
  * &#x1F539;*Optional* - if omitted, defaults to full height range.
  * This object defines the vertical range condition.

## IRuleMatchDrops

This object defines conditions to match the items dropped by the fishing attempt.

<big><pre>
{
&nbsp;&nbsp;"type": enum,
&nbsp;&nbsp;"drops": String[]
}
</pre></big>

* `type`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `WHITELIST`.
  * `WHITELIST`: If any item dropped is in the list, then this match condition passes.
  * `BLACKLIST`: If no item dropped is in the list, then this match condition passes.
  
* `drops`: `String[]`
  * &#x1F539;*Optional* - if omitted, this condition passes.
  * This string array defines items to be matched against the items dropped.
  * Syntax: `domain:path:meta`, meta may be a wildcard `*`. Multiple meta values can be specified using `domain:path:meta,meta,meta`. OreDict values are accepted.
  * Example: `minecraft:stone:0`

## IRuleMatchAngler

This object defines conditions specific to the angler.

<big><pre>
{
&nbsp;&nbsp;"type": enum,
&nbsp;&nbsp;"heldItemMainHand": [IRuleMatchAnglerHeldItemMainHand](#irulematchanglerhelditemmainhand),
&nbsp;&nbsp;"gamestages": [IRuleMatchAnglerGameStage](#irulematchanglergamestage),
&nbsp;&nbsp;"playerName": [IRuleMatchAnglerPlayerName](#irulematchanglerplayername)
}
</pre></big>

* `type`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `ANY`.
  * `PLAYER`: The angler must be a player to match.
  * `NON_PLAYER`: The angler must not be a player to match.
  * `ANY`: The matcher will accept either.<br/>If the angler is a player and `heldItemMainHand`, `gamestages`, or `playerName` is provided, they will be checked.
  
* `heldItemMainHand`: <code>[IRuleMatchAnglerHeldItemMainHand](#irulematchanglerhelditemmainhand)</code>
  * &#x1F539;*Optional* - if omitted, this condition will pass.
  * This object defines conditions for matching held items.

* `gamestages`: <code>[IRuleMatchAnglerGameStage](#irulematchanglergamestage)</code>
  * &#x1F539;*Optional* - if omitted, this condition will pass.
  * This object defines conditions for matching gamestages.
  
* `playerName`: <code>[IRuleMatchAnglerPlayerName](#irulematchanglerplayername)</code>
  * &#x1F539;*Optional* - if omitted, this condition will pass.
  * This object defines conditions for matching player names.

## IRuleMatchAnglerHeldItemMainHand

This object defines conditions for matching held items.

<big><pre>
{
&nbsp;&nbsp;"type": enum,
&nbsp;&nbsp;"items": String[]
}
</pre></big>

* `type`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `WHITELIST`.
  * `WHITELIST`: Passes if the held item is in the list.
  * `BLACKLIST`: Passes if the held item is not in the list.

* `items`: `String[]`
  * &#x1F539;*Optional* - if omitted, all held items will be matched.
  * Syntax: `domain:path:meta`, `meta` can be a wildcard `*`. It is advised to use the meta wildcard `*` when matching tools.
  * Note: To match an empty hand, use `"EMTPY"`; ie. `"items": ["EMPTY"]`

## IRuleMatchAnglerGameStage

This object defines conditions for matching gamestages.

<big><pre>
{
&nbsp;&nbsp;"type": enum,
&nbsp;&nbsp;"require": enum,
&nbsp;&nbsp;"stages": String[]
}
</pre></big>

* `type`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `WHITELIST`.
  * `WHITELIST`: Does not invert the result of this check.
  * `BLACKLIST`: Inverts the result of this check.

* `require`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `ANY`.
  * `ANY`: The player must have any of the listed gamestages to match.
  * `ALL`: The player must have all of the listed gamestages to match.
  
* `stages`: `String[]`
  * This string array lists the gamestages required to match.

## IRuleMatchAnglerPlayerName

This object defines conditions for matching player names.

<big><pre>
{
&nbsp;&nbsp;"type": enum,
&nbsp;&nbsp;"names": String[]
}
</pre></big>

* `type`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `WHITELIST`.
  * `WHITELIST`: Passes if the player name is in the list.
  * `BLACKLIST`: Passes if the player name is not in the list.
  
* `playerName`: `String[]`
  * &#x1F539;*Optional* - if omitted, this condition will pass.
  * This array defines player names to match.

## IRuleMatchBiome

This object defines conditions for matching biomes.

<big><pre>
{
&nbsp;&nbsp;"type": enum,
&nbsp;&nbsp;"ids": String[]
}
</pre></big>

* `type`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `WHITELIST`.
  * `WHITELIST`: If the angler is in a biome in the list, the match passes.
  * `BLACKLIST`: If the angler is in a biome not in the list, the match passes.
  
* `ids`: `String[]`
  * This string array contains the biome ids required to match.
  * Syntax: `domain:path`
  * Example: `minecraft:birch_forest_hills`
  
## IRuleMatchDimension

This object defines conditions for matching dimensions.

<big><pre>
{
&nbsp;&nbsp;"type": enum,
&nbsp;&nbsp;"ids": int[]
}
</pre></big>

* `type`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `WHITELIST`.
  * `WHITELIST`: If the angler is in a dimension in the list, the match passes.
  * `BLACKLIST`: If the angler is in a dimension not in the list, the match passes.
  
* `ids`: `int[]`
  * This array contains the integer id's of the dimensions required to match.
  
## IRangeInt

This object defines a range between a min and max integer value.

<big><pre>
{
&nbsp;&nbsp;"min": int,
&nbsp;&nbsp;"max": int
}
</pre></big>

* `min`: `int`
  * &#x1F539;*Optional* - if omitted, defaults to the minimum value of the enclosing context.
  * This integer defines the minimum value of this range (inclusive).

* `max`: `int`
  * &#x1F539;*Optional* - if omitted, defaults to the maximum value of the enclosing context.
  * This integer defines the maximum value of this range (inclusive).

## IRandomLuckInt

This object defines a range used to select a luck modified random number.

<big><pre>
{
&nbsp;&nbsp;"fixed": int,
&nbsp;&nbsp;"min": int,
&nbsp;&nbsp;"max": int,
&nbsp;&nbsp;"luckModifier": int
}
</pre></big>

Calculation: `random(max - min) + min + luckModifier * luckLevel`

* `fixed`: `int`
  * &#x1F539;*Optional* - if omitted, defaults to using `min` and `max`.
  * The fixed value that will always be used. If a value `> 0` is supplied, it will be used in the calculation instead of a randomly selected value.
  
* `min`: `int`
  * &#x1F539;*Optional* - if omitted, defaults to context.
  * The minimum value that will be randomly selected (inclusive).
  
* `max`: `int`
  * &#x1F539;*Optional* - if omitted, defaults to context.
  * The maximum value that will be randomly selected (inclusive).
  
* `luckModifier`
  * &#x1F539;*Optional* - if omitted, defaults to `0`.
  * This value will be added to the result of the random value for each level of luck the angler has.

## IRuleDrop

This object defines conditions used to select and weight a drop as well as the dropped item itself. 

If the drop is a valid candidate it will be placed into the weighted picker using the luck modified weight described in the <code>[IRuleDropSelector](#iruledropselector)</code>.

<big><pre>
{
&nbsp;&nbsp;"selector": [IRuleDropSelector](#iruledropselector),
&nbsp;&nbsp;"item": [IRuleDropItem](#iruledropitem),
&nbsp;&nbsp;"xp": [IRandomLuckInt](#irandomluckint),
&nbsp;&nbsp;"xpReplaceStrategy": enum
}
</pre></big>

* `selector`: <code>[IRuleDropSelector](#iruledropselector)</code>
  * &#x1F539;*Optional* - if omitted, no selection conditions will apply to the drop's candidacy and the weight will default to `1`.
  * This object defines conditions for the drop's candidacy as well as its chance to drop.
  
* `item`: <code>[IRuleDropItem](#iruledropitem)</code>
  * &#x1F539;*Optional* - if omitted, no item will be dropped if this `IRuleDrop` is selected from the weighted picker.
  * This object defines the item to drop.
  
* `xp`: <code>[IRandomLuckInt](#irandomluckint)</code>
  * &#x1F539;*Optional* - if omitted, defaults to `0`.
  * This object defines how much XP to drop when this drop is selected.
  * This amount will either be added to or completely replace any XP that normally drops, see the enum below.

* `xpReplaceStrategy`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `ADD`.
  * `ADD`: Add the specified xp to any xp dropped.
  * `REPLACE`: Replace any xp dropped with the specified xp.

## IRuleDropSelector

This object acts as a predicate to identify drop candidates. 

If a rule is matched, the `IRuleDropSelector` is queried to determine a drop's candidacy for the weighted picker. If selected, it also supplies the luck modified weight to use when adding the drop to the weighted picker.

<big><pre>
{
&nbsp;&nbsp;"weight": [IRuleDropSelectorWeight](#iruledropselectorweight),
&nbsp;&nbsp;"silktouch": enum,
&nbsp;&nbsp;"luckLevelRequired": int
}
</pre></big>

* `weight`: <code>[IRuleDropSelectorWeight](#iruledropselectorweight)</code>
  * &#x1F539;*Optional* - if omitted, defaults to `1`.
  * This object defines the luck modified weight to use when adding this drop to the weighted picker.

* `silktouch`: `enum`
  * &#x1F539;*Optional* - if omitted, defaults to `ANY`.
  * `REQUIRED`: Silk touch is required on the tool the player is using.
  * `EXCLUDED`: Silk touch must not be on the tool the player is using.
  * `ANY`: Silk touch is overlooked.
  
* `luckLevelRequired`: `int`
  * &#x1F539;*Optional* - if omitted, defaults to `0`.
  * The minimum luck level required for this drop to be a candidate.

## IRuleDropSelectorWeight

This object defines the luck modified weight to use when adding a drop to the weighted picker

<big><pre>
{
&nbsp;&nbsp;"value": int,
&nbsp;&nbsp;"luckModifier": int
}
</pre></big>

Calculation: `value + luckModifier * luckLevel`

* `value`: `int`
  * &#x1F539;*Optional* - if omitted, defaults to `1`.
  * This is the weight used when adding this drop to the weighted picker.
  
* `luckModifier`: `int`
  * &#x1F539;*Optional* - if omitted, defaults to `0`.
  * This value will be added to weight value for each level of luck the angler has.
  
## IRuleDropItem

<big><pre>
{
&nbsp;&nbsp;"items": String[]
&nbsp;&nbsp;"quantity": [IRandomLuckInt](#irandomluckint)
}
</pre></big>

This defines the item for the `IRuleDrop`.

* `items`: `String[]`
  * This array contains a list of items, of which one will be randomly selected for the drop. Each item in the list has an equal chance of being selected for the drop.
  * Syntax: `domain:path:meta#nbt`, meta may be a wildcard `*`, see experimental feature below.
  * NBT: the best way to generate a string with the proper NBT encoding is to hold the dropped item and use the `/dropt hand` command
  * &#x1F538;*Experimental*: OreDict entries in this array, like `ore:logWood`, are permitted and will be expanded to the best of the system's ability. *This feature is experimental and may not produce the desired results.*
  * &#x1F538;*Experimental*: If a wildcard value `*` is supplied for the meta, the system will attempt to expand that item into all of its valid subtypes. *This feature is experimental and may not produce the desired results.*

* `quantity`: <code>[IRandomLuckInt](#irandomluckint)</code>
  * &#x1F539;*Optional* - if omitted, defaults to `1`
  * This uses a random, luck modified range to determine how many of this item will be dropped if selected.