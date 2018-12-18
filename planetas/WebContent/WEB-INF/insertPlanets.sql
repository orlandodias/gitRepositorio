use orlando_caa;
delete from tb_planeta where id>=5;
insert into tb_planeta(nome,clima,terreno) values ('Alderaan','temperate','grasslands, mountains');
insert into tb_planeta(nome,clima,terreno) values ('Yavin IV','temperate, tropical','jungle, rainforests');
insert into tb_planeta(nome,clima,terreno) values ('Hoth','frozen','tundra, ice caves, mountain ranges');
insert into tb_planeta(nome,clima,terreno) values ('Dagobah','murky','swamp, jungles');

select * from tb_planeta;
