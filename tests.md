# 1
- exception thrown for 0 size
- no exception for size > 0

# 2
For new gridImpl
- isOver is false
- winner is NONE
- currentPlayer (first) is WHITE

# 3
- Test that all pieces in grid are NONE
    - malformed game implementations can cause this to be false even if grid impl is valid
- test independence of copy and original (copy not a reference to original)

# 4
- getPiece correctly returns piece
- currentPlayer switches to black
- Repeat above two for another turn

# 5
- exception thrown when moving to already occupied spot
- exception thrown when moving out of bound
# 6
- get moves returns all grid positions for empty

# 7
- game copy states same as original immediately after copy
- game copy separate instance - moves not reflected

# 8
- win state detected
    - for black
    - for white
    - win state not reflected in copy
- drawn state detected
    - isOver only true for drawn if grid completely filled