package com.asterexcrisys.brainfuck.models.dialects;

@SuppressWarnings("unused")
public enum DialectType {

    BRAINFUCK(new Dialect(new String[]{"^", "v", "=", ">", "<", "+", "-", "*", ":", "%", "#", "[", "]", ",", ".", "/", "\\"})),
    TROLLSCRIPT(new Dialect(new String[]{"roo", "oor", "oro", "ooo", "ool", "olo", "oll", "rro", "orr", "ror", "rrr", "loo", "lol", "llo", "lll", "rol", "lor"})),
    OOK(new Dialect(new String[]{"Okk. Okk!", "Okk. Okk?", "Okk. Okk.", "Ook. Ook?", "Ook? Ook.", "Ook. Ook.", "Ook! Ook!", "Oko. Oko.", "Oko. Oko!", "Oko. Oko?", "Oko! Oko?", "Ook! Ook.", "Ook. Ook!", "Ook! Ook?", "Ook? Ook!", "Koo. Koo!", "Koo. Koo?"})),
    COW(new Dialect(new String[]{"m00", "M00", "m0o", "moO", "mOo", "MoO", "MOo", "OOM", "oom", "00M", "00m", "MOO", "moo", "Moo", "mOO", "O0M", "0OM"}));

    private final Dialect dialect;

    DialectType(Dialect dialect) {
        this.dialect = dialect;
    }

    public Dialect dialect() {
        return dialect;
    }

}