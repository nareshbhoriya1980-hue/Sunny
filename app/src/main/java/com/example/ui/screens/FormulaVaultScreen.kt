package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FormulaCard
import com.example.data.model.Subject
import com.example.ui.theme.AcademicBackground
import com.example.ui.theme.AcademicElectricViolet
import com.example.ui.theme.AcademicPrimary
import com.example.ui.theme.CriticalAlertCrimson
import com.example.ui.theme.HighYieldGold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormulaVaultScreen(
    formulas: List<FormulaCard>,
    selectedGrade: Int,
    onGradeSelected: (Int) -> Unit,
    onExplainFormula: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf<Subject?>(null) }

    val filtered = formulas.filter { formula ->
        val queryMatches = searchQuery.isBlank() ||
                formula.name.contains(searchQuery, ignoreCase = true) ||
                formula.latexFormula.contains(searchQuery, ignoreCase = true) ||
                formula.chapterName.contains(searchQuery, ignoreCase = true)
        val subjectMatches = selectedSubject == null || formula.subject == selectedSubject
        queryMatches && subjectMatches
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AcademicBackground)
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search formulas, dimensions, or caveats...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AcademicElectricViolet,
                unfocusedBorderColor = Color(0xFFCBD5E1),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("formula_search_bar")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Subject filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(percent = 50),
                color = if (selectedSubject == null) AcademicPrimary else Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                modifier = Modifier.clickable { selectedSubject = null }
            ) {
                Text(
                    text = "All Subjects",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (selectedSubject == null) Color.White else AcademicPrimary
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            Subject.entries.forEach { subj ->
                val isSelected = selectedSubject == subj
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = if (isSelected) AcademicPrimary else Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    modifier = Modifier.clickable { selectedSubject = subj }
                ) {
                    Text(
                        text = subj.displayName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else AcademicPrimary
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered) { formula ->
                FormulaCardItem(
                    formula = formula,
                    onExplain = { onExplainFormula("${formula.name}: ${formula.latexFormula}") }
                )
            }
        }
    }
}

@Composable
private fun FormulaCardItem(
    formula: FormulaCard,
    onExplain: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = formula.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AcademicPrimary
                        )
                    )
                    Text(
                        text = "${formula.subject.displayName} • ${formula.chapterName}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = AcademicElectricViolet,
                    modifier = Modifier.clickable { onExplain() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Explain",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Explain AI",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // LaTeX Formula Block
            Surface(
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = formula.latexFormula,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = AcademicPrimary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dimensional Formula & Units
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SI Units: ${formula.siUnits}",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.DarkGray)
                )
                Text(
                    text = "Dimensions: ${formula.dimensionalFormula}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AcademicElectricViolet
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Crucial Caveats & Boundary Warnings
            Surface(
                color = Color(0xFFFEF3C7).copy(alpha = 0.6f),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WarningAmber,
                        contentDescription = "Caveat",
                        tint = HighYieldGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Boundary Caveat: ${formula.crucialCaveats}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF92400E),
                            lineHeight = 16.sp
                        )
                    )
                }
            }
        }
    }
}
