#include "gamma.h"

// Gamma lookup table for a 2.2 gamma curve.
// Generated using src/jvmMain/kotlin/baaahs/util/GammaGenerator.kt.
static const GammaData _table2_2[] = {
    {0,0b0},{0,0b0},{0,0b0},{0,0b0},{0,0b0},{0,0b0},{0,0b10000000},{0,0b10000000},
    {0,0b10000000},{0,0b10000000},{0,0b10001000},{0,0b10001000},{0,0b10001000},{0,0b10101000},{0,0b10101000},{0,0b10101010},
    {0,0b11101010},{0,0b11101010},{0,0b11101110},{0,0b11111110},{0,0b11111111},{1,0b0},{1,0b10000000},{1,0b10001000},
    {1,0b10101000},{1,0b10101010},{1,0b11101010},{1,0b11111110},{1,0b11111111},{2,0b10000000},{2,0b10001000},{2,0b10101010},
    {2,0b11101010},{2,0b11111110},{3,0b0},{3,0b10001000},{3,0b10101000},{3,0b11101010},{3,0b11111110},{4,0b10000000},
    {4,0b10101000},{4,0b11101010},{4,0b11111110},{5,0b10000000},{5,0b10101000},{5,0b11101010},{5,0b11111110},{6,0b10000000},
    {6,0b10101010},{6,0b11101110},{7,0b10000000},{7,0b10101000},{7,0b11101110},{8,0b0},{8,0b10101000},{8,0b11101110},
    {9,0b10000000},{9,0b10101010},{9,0b11101110},{10,0b10000000},{10,0b11101010},{10,0b11111111},{11,0b10101000},{11,0b11101110},
    {12,0b10000000},{12,0b11101010},{13,0b0},{13,0b10101010},{13,0b11111110},{14,0b10101000},{14,0b11111110},{15,0b10001000},
    {15,0b11101110},{16,0b10001000},{16,0b11101110},{17,0b10001000},{17,0b11101110},{18,0b10001000},{18,0b11111110},{19,0b10101000},
    {19,0b11111110},{20,0b10101010},{21,0b0},{21,0b11101010},{22,0b10000000},{22,0b11101110},{23,0b10101000},{23,0b11111111},
    {24,0b10101010},{25,0b10000000},{25,0b11101110},{26,0b10101000},{27,0b10000000},{27,0b11101110},{28,0b10101000},{29,0b0},
    {29,0b11101110},{30,0b10101000},{31,0b10000000},{31,0b11101110},{32,0b10101010},{33,0b10001000},{33,0b11111111},{34,0b11101110},
    {35,0b10101010},{36,0b10001000},{36,0b11111111},{37,0b11101110},{38,0b10101010},{39,0b10001000},{40,0b10000000},{40,0b11111110},
    {41,0b11101110},{42,0b10101010},{43,0b10101000},{44,0b10001000},{45,0b10000000},{45,0b11111110},{46,0b11101110},{47,0b11101010},
    {48,0b11101010},{49,0b10101010},{50,0b10101000},{51,0b10001000},{52,0b10001000},{53,0b10000000},{54,0b10000000},{55,0b0},
    {55,0b11111111},{56,0b11111111},{57,0b11111110},{58,0b11111110},{59,0b11111110},{60,0b11111110},{61,0b11111110},{62,0b11111110},
    {63,0b11111111},{65,0b0},{66,0b0},{67,0b10000000},{68,0b10000000},{69,0b10001000},{70,0b10101000},{71,0b10101000},
    {72,0b10101010},{73,0b11101010},{74,0b11101110},{75,0b11111110},{77,0b0},{78,0b10001000},{79,0b10101000},{80,0b10101010},
    {81,0b11101110},{82,0b11111110},{84,0b10000000},{85,0b10001000},{86,0b10101010},{87,0b11101110},{88,0b11111111},{90,0b10001000},
    {91,0b10101010},{92,0b11101110},{93,0b11111111},{95,0b10001000},{96,0b10101010},{97,0b11111110},{99,0b10000000},{100,0b10101010},
    {101,0b11111110},{103,0b10000000},{104,0b10101010},{105,0b11111110},{107,0b10001000},{108,0b11101010},{109,0b11111111},{111,0b10101000},
    {112,0b11101110},{114,0b10001000},{115,0b11101010},{117,0b10000000},{118,0b10101010},{119,0b11111111},{121,0b10101000},{122,0b11111110},
    {124,0b10101000},{125,0b11111110},{127,0b10101000},{128,0b11111110},{130,0b10101000},{131,0b11111110},{133,0b10101010},{135,0b0},
    {136,0b11101010},{138,0b10000000},{139,0b11101110},{141,0b10101000},{142,0b11111110},{144,0b10101010},{146,0b10000000},{147,0b11101110},
    {149,0b10101000},{151,0b10000000},{152,0b11101110},{154,0b10101000},{156,0b10000000},{157,0b11101110},{159,0b10101010},{161,0b10000000},
    {162,0b11111110},{164,0b11101010},{166,0b10101000},{168,0b10000000},{169,0b11111110},{171,0b11101010},{173,0b10101000},{175,0b10001000},
    {176,0b11111111},{178,0b11101110},{180,0b11101010},{182,0b10101010},{184,0b10001000},{186,0b10000000},{187,0b11111111},{189,0b11111110},
    {191,0b11101110},{193,0b11101010},{195,0b10101010},{197,0b10101000},{199,0b10101000},{201,0b10001000},{203,0b10001000},{205,0b10000000},
    {207,0b10000000},{209,0b10000000},{211,0b10000000},{213,0b0},{215,0b0},{217,0b10000000},{219,0b10000000},{221,0b10000000},
    {223,0b10000000},{225,0b10001000},{227,0b10001000},{229,0b10101000},{231,0b10101000},{233,0b10101010},{235,0b11101010},{237,0b11101110},
    {239,0b11111110},{241,0b11111111},{244,0b10000000},{246,0b10001000},{248,0b10101010},{250,0b11101010},{252,0b11101110},{255,0b0}
};


uint8_t Gamma::Correct22NoDither(uint8_t value) {
    return _table2_2[value].value;
}

uint8_t Gamma::Correct22(uint8_t value, uint32_t frameNumber, uint32_t pixelIndex) {
    uint8_t baseValue = _table2_2[value].value;
    uint8_t dither = _table2_2[value].dither;
    uint8_t ditherOffset = (frameNumber + pixelIndex) % 8;
    if (dither & (1u << ditherOffset))
        return baseValue + 1;
    else
        return baseValue;
}
