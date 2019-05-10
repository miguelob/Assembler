-- ICAI-RiSC-16 code.
-- Archivo fuente: C:\Users\oleob\OneDrive - Universidad Pontificia Comillas\ICAI_2GITT\Java\Ensamblador\Assembler\rom.asm.

library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity ROM is
  port(
    clk: in std_logic; -- La ROM es síncrona
    en_pc: in std_logic; -- Y tiene un enable
    dir: in std_logic_vector(4 downto 0); -- Bus de direcciones
    dat: out std_logic_vector(15 downto 0) ); -- Salida de datos
end ROM;

architecture Behavioural of ROM is
  -- Se declara un tipo de datos para albergar la memoria de programa 
  type mem_t is array (0 to 26) of std_logic_vector(15 downto 0);
  signal memoria : mem_t:= (-- Se crea la señal memoria con el contenido
                            -- del programa.
    16#0000# => X"2105", -- 	addi r2, r0, 5
    16#0001# => X"6400",
    16#0002# => X"2489", -- 	la r1, fact
    16#0003# => X"e700", -- 	jalr r1, r6
    16#0004# => X"0450", -- 	add r5, r1, r0
    16#0005# => X"2d81", -- 	addi r3, r3, 1
    16#0006# => X"3202", -- 	addi r4, r4, 2
    16#0007# => X"0410", -- fin:	add r1, r1, r0
    16#0008# => X"c07e", -- 	beq r0, r0, fin
    16#0009# => X"2081", -- fact:	addi r1, r0, 1
    16#000a# => X"0898", -- 	sltu r1, r2, r1
    16#000b# => X"c402", -- 	beq r1, r0, NoRet
    16#000c# => X"2081", -- 	addi r1, r0, 1
    16#000d# => X"f800", -- 	jalr r6, r0
    16#000e# => X"3ffe", -- NoRet:	addi r7, r7, -2
    16#000f# => X"9f01", -- 	sw r6, r7, 1
    16#0010# => X"9d00", -- 	sw r2, r7, 0
    16#0011# => X"297f", -- 	addi r2, r2, -1
    16#0012# => X"6400",
    16#0013# => X"2489", -- 	la r1, fact
    16#0014# => X"e700", -- 	jalr r1, r6
    16#0015# => X"bd00", -- 	lw r2, r7, 0
    16#0016# => X"0893", -- 	mulu r1, r2, r1
    16#0017# => X"bf01", -- 	lw r6, r7, 1
    16#0018# => X"3f82", -- 	addi r7, r7, 2
    16#0019# => X"f800", -- 	jalr r6, r0
    others => X"0000"); -- Para las posiciones sin inicializar
  begin
    mem_rom: process(clk)
    begin
      if clk'event and clk = '1' then
        if en_pc = '1' then          dat <= memoria(to_integer(unsigned(dir)));
        end if;
      end if;
    end process mem_rom;
  end architecture Behavioural;
