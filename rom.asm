	addi r2, r0, 5
	la r1, fact
	jalr r1, r6
	add r5, r1, r0
	addi r3, r3, 1
	addi r4, r4, 2
fin:	add r1, r1, r0
	beq r0, r0, fin
	
	
fact:	addi r1, r0, 1
	sltu r1, r2, r1
	beq r1, r0, NoRet
	addi r1, r0, 1
	jalr r6, r0
		
NoRet:	addi R7, r7, -2
	sw r6, r7, 1
	sw r2, r7, 0
	addi r2, r2, -1
	la r1, fact
	jalr r1, r6
	lw r2, r7, 0
	mulu r1, r2, r1
	lw r6, r7, 1
	addi r7, r7, 2
	jalr r6, r0
