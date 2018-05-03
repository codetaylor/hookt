## Example #1

Replace all drops with string when using a copper fishing rod from ThermalFoundation. If the angler's luck is >= 1, there is a high probability that they will fish up a diamond instead of string.

```json
{
  "rules": [
    {
      "match": {
        "angler": {
          "heldItemMainHand": {
            "items": ["thermalfoundation:tool.fishing_rod_copper:*"]
          }
        }
      },
      "drops": [
        {
          "item": {
            "selector": {
              "weight": {
                "value": 1000
              },
              "luckLevelRequired": 1
            },
            "items": [
              "minecraft:diamond"
            ]
          }
        },
        {
          "item": {
            "items": [
              "minecraft:string"
            ]
          }
        }
      ]
    }
  ]
}
```