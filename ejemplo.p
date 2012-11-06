PROGRAM prueba;
	VAR
		X:BOOLEAN;
	BEGIN
		X:=(NOT FALSE AND TRUE OR FALSE OR (1*5=5));
		IF X THEN 
		BEGIN
			IF TRUE THEN
			BEGIN
				WRITE (77);
			END
			else
			begin
				WRITE (5);
			end;
		END;
END.